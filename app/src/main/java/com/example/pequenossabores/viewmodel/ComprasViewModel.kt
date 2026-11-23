package com.example.pequenossabores.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pequenossabores.data.model.*
import com.example.pequenossabores.data.repository.*
import com.example.pequenossabores.utils.NetworkUtils
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ComprasViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = ComprasRepository()
    private val sesion = SesionPreferences(application)
    private val correo: String get() = sesion.correoActivo().orEmpty()

    val peque: StateFlow<PequeEntity?> = PequenosSaboresRepository.crear(application).observarPeque()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    var items by mutableStateOf<List<ItemCompra>>(emptyList())
        private set
    var cargando by mutableStateOf(true)
        private set
    var mensaje by mutableStateOf<String?>(null)
        private set
    var actualizado by mutableStateOf(false)
        private set
    var filtro by mutableStateOf("Todos")
        private set

    init { cargar() }

    // ---------- Estado derivado ----------
    val total: Int get() = items.size
    val comprados: Int get() = items.count { it.comprado == 1 }
    val porComprar: Int get() = total - comprados
    val porcentaje: Int get() = if (total == 0) 0 else comprados * 100 / total

    val grupos: List<Pair<String, List<ItemCompra>>>
        get() {
            val base = if (filtro == "Todos") items else items.filter { it.categoria == filtro }
            return Clasificador.orden.mapNotNull { c ->
                base.filter { it.categoria == c }.takeIf { it.isNotEmpty() }?.let { c to it }
            }
        }

    fun filtrar(categoria: String) { filtro = categoria }

    // ---------- Operaciones con el servidor ----------
    fun cargar() {
        viewModelScope.launch {
            cargando = true
            mensaje = null
            if (!NetworkUtils.hayConexion(getApplication<Application>())) {
                mensaje = "Sin conexión a la red"
                actualizado = false
                cargando = false
                return@launch
            }
            try {
                items = repo.listar(correo)
                actualizado = true
            } catch (e: Exception) {
                mensaje = "Error de conexión con el servidor"
                actualizado = false
            }
            cargando = false
        }
    }

    fun alternar(item: ItemCompra) {
        val nuevo = if (item.comprado == 1) 0 else 1
        items = items.map { if (it.id == item.id) it.copy(comprado = nuevo) else it }   // se ve al instante
        viewModelScope.launch {
            try {
                repo.marcar(correo, item.id, nuevo == 1)
            } catch (e: Exception) {
                items = items.map { if (it.id == item.id) it.copy(comprado = item.comprado) else it }
                mensaje = "Error de conexión"
            }
        }
    }

    fun agregar(nombre: String, cantidad: String) {
        val n = nombre.trim()
        if (n.isEmpty()) return
        viewModelScope.launch {
            if (!NetworkUtils.hayConexion(getApplication<Application>())) {
                mensaje = "Sin conexión a la red"
                return@launch
            }
            try {
                repo.agregar(correo, n.take(100), cantidad.trim().take(50), Clasificador.categoria(n))
                items = repo.listar(correo)
                mensaje = null
            } catch (e: Exception) {
                mensaje = "Error de conexión con el servidor"
            }
        }
    }

    fun vaciarMarcados() {
        viewModelScope.launch {
            try {
                if (repo.vaciarMarcados(correo)) items = items.filter { it.comprado == 0 }
            } catch (e: Exception) {
                mensaje = "Error de conexión con el servidor"
            }
        }
    }

    fun textoCompartir(): String = buildString {
        appendLine("Mi lista de compras · Pequeños Sabores")
        Clasificador.orden.forEach { c ->
            val lista = items.filter { it.categoria == c }
            if (lista.isNotEmpty()) {
                appendLine()
                appendLine(c)
                lista.forEach { appendLine("${if (it.comprado == 1) "✔" else "•"} ${it.nombre} ${it.cantidad}".trim()) }
            }
        }
    }
}
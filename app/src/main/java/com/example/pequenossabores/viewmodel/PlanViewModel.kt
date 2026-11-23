package com.example.pequenossabores.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pequenossabores.data.model.*
import com.example.pequenossabores.data.repository.*
import com.example.pequenossabores.utils.NetworkUtils
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PlanViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = PequenosSaboresRepository.crear(application)
    private val compras = ComprasRepository()
    private val sesion = SesionPreferences(application)

    val peque: StateFlow<PequeEntity?> = repo.observarPeque()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    var diaSeleccionado by mutableIntStateOf(0)
        private set
    var cargando by mutableStateOf(true)
        private set
    var generando by mutableStateOf(false)
        private set
    var mensaje by mutableStateOf<String?>(null)
        private set

    private var pool by mutableStateOf<List<Receta>>(emptyList())
    private var orden by mutableStateOf<List<Int>>(emptyList())

    init { cargar() }

    fun cargar() {
        viewModelScope.launch {
            cargando = true
            mensaje = null
            val resultado = if (NetworkUtils.hayConexion(getApplication<Application>()))
                RecetasRepository.obtenerPool()
            else ResultadoPool(RecetasData.recetas, false)

            pool = resultado.recetas
            orden = pool.indices.take(8).toList()
            if (!resultado.desdeApi) {
                mensaje = "Sin conexión con TheMealDB: mostrando el catálogo guardado en la app."
            }
            cargando = false
        }
    }

    val semana: List<DiaPlan>
        get() = if (pool.size < 8 || orden.size < 8) emptyList()
        else RecetasData.armarSemana(orden.map { pool[it] })

    val totalIngredientes: Int
        get() = semana.flatMap { it.recetas }.flatMap { it.ingredientes }
            .map { it.nombre.ifBlank { it.texto }.lowercase() }.distinct().size

    fun seleccionarDia(i: Int) { diaSeleccionado = i }

    /** "Regenerar menú": elige otras 8 recetas del pool. */
    fun regenerar() {
        if (pool.size >= 8) orden = pool.indices.shuffled().take(8)
    }

    /** Envía los ingredientes del plan al servidor PHP/MySQL. */
    fun generarLista(onListo: () -> Unit, onError: (String) -> Unit) {
        if (generando) return
        viewModelScope.launch {
            if (!NetworkUtils.hayConexion(getApplication<Application>())) {
                onError("Sin conexión a la red")
                return@launch
            }
            generando = true
            val items = semana.flatMap { it.recetas }.flatMap { it.ingredientes }.map { ing ->
                val nombre = ing.nombre.ifBlank { ing.texto }.take(100)
                ItemEnvio(nombre, ing.cantidad.take(50), ing.categoria.ifBlank { Clasificador.categoria(nombre) })
            }.distinctBy { it.nombre.lowercase() }

            try {
                if (compras.agregarLote(sesion.correoActivo().orEmpty(), items)) onListo()
                else onError("El servidor no pudo guardar la lista")
            } catch (e: Exception) {
                onError("Error de conexión con el servidor")
            }
            generando = false
        }
    }
}
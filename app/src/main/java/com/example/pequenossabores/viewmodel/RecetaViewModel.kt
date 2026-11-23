package com.example.pequenossabores.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pequenossabores.data.model.PequeEntity
import com.example.pequenossabores.data.model.Receta
import com.example.pequenossabores.data.repository.PequenosSaboresRepository
import com.example.pequenossabores.data.repository.RecetasRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RecetaViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = PequenosSaboresRepository.crear(application)

    val peque: StateFlow<PequeEntity?> = repo.observarPeque()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val favoritos: StateFlow<Set<Int>> = repo.observarFavoritos()
        .map { it.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    var receta by mutableStateOf<Receta?>(null)
        private set
    var errorCarga by mutableStateOf(false)
        private set

    var marcados by mutableStateOf(setOf<Int>())
        private set

    fun cargar(id: Int) {
        if (receta?.id == id) return
        viewModelScope.launch {
            errorCarga = false
            receta = RecetasRepository.obtenerReceta(id)
            if (receta == null) errorCarga = true
        }
    }

    fun alternarMarcado(indice: Int) {
        marcados = if (indice in marcados) marcados - indice else marcados + indice
    }

    fun alternarFavorito(recetaId: Int) {
        viewModelScope.launch {
            repo.alternarFavorito(recetaId, recetaId in favoritos.value)
        }
    }
}
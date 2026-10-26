package com.example.pequenossabores.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pequenossabores.data.model.PequeEntity
import com.example.pequenossabores.data.repository.PequenosSaboresRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RecetaViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = PequenosSaboresRepository.crear(application)

    val peque: StateFlow<PequeEntity?> = repo.observarPeque()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val favoritos: StateFlow<Set<Int>> = repo.observarFavoritos()
        .map { it.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    /** Ingredientes marcados en la lista (índices). */
    var marcados by mutableStateOf(setOf<Int>())
        private set

    fun alternarMarcado(indice: Int) {
        marcados = if (indice in marcados) marcados - indice else marcados + indice
    }

    fun alternarFavorito(recetaId: Int) {
        viewModelScope.launch {
            repo.alternarFavorito(recetaId, recetaId in favoritos.value)
        }
    }
}
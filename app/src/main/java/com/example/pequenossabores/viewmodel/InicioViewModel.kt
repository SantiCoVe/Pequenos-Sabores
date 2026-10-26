package com.example.pequenossabores.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pequenossabores.data.model.*
import com.example.pequenossabores.data.repository.PequenosSaboresRepository
import kotlinx.coroutines.flow.*

data class InicioUiState(
    val menu: MenuDelDia,
    val progreso: List<ProgresoGrupo>
) {
    val total: Int get() = progreso.map { it.porcentaje }.average().toInt()
}

class InicioViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = PequenosSaboresRepository.crear(application)

    val estado = InicioUiState(repo.menuDelDia(), repo.progresoSemanal())

    val usuario: StateFlow<UsuarioEntity?> = repo.observarUsuarioActivo()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val peque: StateFlow<PequeEntity?> = repo.observarPeque()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}
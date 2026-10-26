package com.example.pequenossabores.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pequenossabores.data.model.DiaPlan
import com.example.pequenossabores.data.model.PequeEntity
import com.example.pequenossabores.data.repository.PequenosSaboresRepository
import com.example.pequenossabores.data.repository.RecetasData
import kotlinx.coroutines.flow.*

class PlanViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = PequenosSaboresRepository.crear(application)

    val peque: StateFlow<PequeEntity?> = repo.observarPeque()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    var diaSeleccionado by mutableIntStateOf(0)
        private set

    private var orden by mutableStateOf(RecetasData.recetas.map { it.id })

    val semana: List<DiaPlan> get() = RecetasData.armarSemana(orden)

    fun seleccionarDia(i: Int) { diaSeleccionado = i }

    /** "Regenerar menú": reparte las recetas en otro orden. */
    fun regenerar() { orden = orden.shuffled() }
}
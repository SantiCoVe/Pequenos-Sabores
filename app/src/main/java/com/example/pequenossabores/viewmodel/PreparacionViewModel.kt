package com.example.pequenossabores.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pequenossabores.data.model.Receta
import com.example.pequenossabores.data.repository.RecetasRepository
import kotlinx.coroutines.launch

class PreparacionViewModel : ViewModel() {

    var pasoActual by mutableIntStateOf(0)
        private set
    var receta by mutableStateOf<Receta?>(null)
        private set
    var errorCarga by mutableStateOf(false)
        private set

    fun cargar(id: Int) {
        if (receta?.id == id) return
        viewModelScope.launch {
            errorCarga = false
            receta = RecetasRepository.obtenerReceta(id)
            if (receta == null) errorCarga = true
        }
    }

    /** Devuelve true si ya era el último paso. */
    fun siguiente(totalPasos: Int): Boolean {
        return if (pasoActual < totalPasos - 1) { pasoActual++; false } else true
    }

    fun anterior() {
        if (pasoActual > 0) pasoActual--
    }
}
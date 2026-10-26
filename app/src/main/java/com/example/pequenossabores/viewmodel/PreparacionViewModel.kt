package com.example.pequenossabores.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class PreparacionViewModel : ViewModel() {

    var pasoActual by mutableIntStateOf(0)
        private set

    /** Devuelve true si ya era el último paso (receta terminada). */
    fun siguiente(totalPasos: Int): Boolean {
        return if (pasoActual < totalPasos - 1) {
            pasoActual++
            false
        } else true
    }

    fun anterior() {
        if (pasoActual > 0) pasoActual--
    }
}
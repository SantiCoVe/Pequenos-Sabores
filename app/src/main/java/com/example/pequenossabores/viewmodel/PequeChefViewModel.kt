
package com.example.pequenossabores.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PequeChefViewModel : ViewModel() {

    private val _ingredientes = MutableStateFlow("")

    val ingredientes = _ingredientes.asStateFlow()

    fun actualizarIngredientes(valor: String) {
        _ingredientes.value = valor
    }

    fun obtenerIngredientes(): String {
        return _ingredientes.value.trim()
    }

    fun puedeGenerarReceta(): Boolean {
        return _ingredientes.value.isNotBlank()
    }

    fun limpiarIngredientes() {
        _ingredientes.value = ""
    }
}

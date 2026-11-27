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
import kotlinx.coroutines.flow.map

data class InicioUiState(
    val progreso: List<ProgresoGrupo>
) {
    val total: Int
        get() = if (progreso.isEmpty()) 0
        else progreso.map { it.porcentaje }.average().toInt()
}

class InicioViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = PequenosSaboresRepository.crear(application)

    private val progresoInicial = InicioUiState(
        listOf(
            ProgresoGrupo("Frutas y verduras", "🥬", 0),
            ProgresoGrupo("Lácteos y alternativas", "🥛", 0),
            ProgresoGrupo("Cereales y harinas", "🌾", 0),
            ProgresoGrupo("Proteínas", "🍗", 0)
        )
    )

    val estado: StateFlow<InicioUiState> =
        repo.observarProgresoSemanal()
            .map { progreso -> InicioUiState(progreso) }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                progresoInicial
            )

    val usuario: StateFlow<UsuarioEntity?> = repo.observarUsuarioActivo()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val peque: StateFlow<PequeEntity?> = repo.observarPeque()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    var recetaRecomendada by mutableStateOf<Receta?>(null)
        private set

    var cargandoReceta by mutableStateOf(true)
        private set

    init {
        cargarRecetaRecomendada()
    }

    var consejoDelDia by mutableStateOf(
        ConsejosNutricionales.obtenerDelDia()
    )
        private set

    fun cargarRecetaRecomendada() {
        viewModelScope.launch {
            cargandoReceta = true

            try {
                val resultado =
                    if (NetworkUtils.hayConexion(getApplication<Application>())) {
                        RecetasRepository.obtenerPool()
                    } else {
                        ResultadoPool(RecetasData.recetas, false)
                    }

                recetaRecomendada = resultado.recetas.randomOrNull()
                    ?: RecetasData.recetas.firstOrNull()

            } catch (e: Exception) {
                recetaRecomendada = RecetasData.recetas.firstOrNull()
            } finally {
                cargandoReceta = false
            }
        }
    }
}
package com.example.pequenossabores.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pequenossabores.data.repository.PequenosSaboresRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class PerfilUiState(
    val avatar: Int = 0,
    val nombre: String = "",
    val edadMeses: Int = 8,
    val preferencias: Set<String> = setOf("Frutas dulces", "Purés suaves", "Texturas BLW"),
    val noLeGustan: String = "",
    val alergias: Set<String> = setOf("Ninguna"),
    val guardando: Boolean = false
) {
    val valido: Boolean get() = nombre.isNotBlank()
}

class PerfilViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = PequenosSaboresRepository.crear(application)

    private val _estado = MutableStateFlow(PerfilUiState())
    val estado: StateFlow<PerfilUiState> = _estado.asStateFlow()

    init {
        // Si ya existe un perfil en Room, se precarga para poder editarlo
        viewModelScope.launch {
            repo.obtenerPeque()?.let { p ->
                _estado.update {
                    it.copy(
                        avatar = p.avatar,
                        nombre = p.nombre,
                        edadMeses = p.edadMeses,
                        preferencias = p.preferenciasSet(),
                        noLeGustan = p.noLeGustan,
                        alergias = p.alergiasSet().ifEmpty { setOf("Ninguna") }
                    )
                }
            }
        }
    }

    fun onAvatar(i: Int) = _estado.update { it.copy(avatar = i) }
    fun onNombre(v: String) = _estado.update { it.copy(nombre = v) }
    fun onEdad(meses: Int) = _estado.update { it.copy(edadMeses = meses) }
    fun onNoLeGustan(v: String) = _estado.update { it.copy(noLeGustan = v) }

    fun togglePreferencia(p: String) = _estado.update {
        it.copy(preferencias = if (p in it.preferencias) it.preferencias - p else it.preferencias + p)
    }

    /** "Ninguna" es excluyente con las demás alergias. */
    fun toggleAlergia(a: String) = _estado.update {
        val nuevas = when {
            a == "Ninguna" -> setOf("Ninguna")
            a in it.alergias -> it.alergias - a
            else -> (it.alergias - "Ninguna") + a
        }
        it.copy(alergias = nuevas.ifEmpty { setOf("Ninguna") })
    }

    fun guardar(onListo: () -> Unit) {
        val s = _estado.value
        if (!s.valido || s.guardando) return
        viewModelScope.launch {
            _estado.update { it.copy(guardando = true) }
            repo.guardarPeque(s.avatar, s.nombre, s.edadMeses, s.preferencias, s.noLeGustan, s.alergias)
            _estado.update { it.copy(guardando = false) }
            onListo()
        }
    }
}
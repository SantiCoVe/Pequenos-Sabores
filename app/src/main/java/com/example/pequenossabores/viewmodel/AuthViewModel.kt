package com.example.pequenossabores.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pequenossabores.data.model.ResultadoAuth
import com.example.pequenossabores.data.model.RolFamiliar
import com.example.pequenossabores.data.repository.PequenosSaboresRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class AuthUiState(
    val modoLogin: Boolean = false,
    val nombre: String = "",
    val correo: String = "",
    val contrasena: String = "",
    val verContrasena: Boolean = false,
    val rol: RolFamiliar = RolFamiliar.MAMA,
    val aceptaTerminos: Boolean = true,
    val cargando: Boolean = false,
    val error: String? = null
) {
    /** 0..3 */
    val nivelSeguridad: Int get() = listOf(
        contrasena.length >= 8,
        contrasena.any { it.isDigit() },
        contrasena.any { !it.isLetterOrDigit() || it.isUpperCase() }
    ).count { it }

    val etiquetaSeguridad: String get() = when (nivelSeguridad) {
        0, 1 -> "Débil"; 2 -> "Buena"; else -> "Excelente"
    }

    val formularioValido: Boolean get() =
        if (modoLogin) correo.contains("@") && contrasena.isNotBlank()
        else nombre.isNotBlank() && correo.contains("@") && contrasena.length >= 6 && aceptaTerminos
}

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = PequenosSaboresRepository.crear(application)

    private val _estado = MutableStateFlow(AuthUiState())
    val estado: StateFlow<AuthUiState> = _estado.asStateFlow()

    fun setModoLogin(login: Boolean) = _estado.update { it.copy(modoLogin = login, error = null) }
    fun onNombre(v: String) = _estado.update { it.copy(nombre = v) }
    fun onCorreo(v: String) = _estado.update { it.copy(correo = v, error = null) }
    fun onContrasena(v: String) = _estado.update { it.copy(contrasena = v, error = null) }
    fun toggleVerContrasena() = _estado.update { it.copy(verContrasena = !it.verContrasena) }
    fun onRol(r: RolFamiliar) = _estado.update { it.copy(rol = r) }
    fun onTerminos(v: Boolean) = _estado.update { it.copy(aceptaTerminos = v) }

    fun enviar(onExito: (Boolean) -> Unit) {
        val s = _estado.value
        if (!s.formularioValido || s.cargando) return

        viewModelScope.launch {
            _estado.update { it.copy(cargando = true, error = null) }

            val resultado = if (s.modoLogin)
                repo.iniciarSesion(s.correo, s.contrasena)
            else
                repo.registrar(s.nombre, s.correo, s.contrasena, s.rol)

            _estado.update {
                it.copy(
                    cargando = false,
                    error = when (resultado) {
                        ResultadoAuth.OK -> null
                        ResultadoAuth.CORREO_EXISTENTE -> "Ese correo ya está registrado. Inicia sesión."
                        ResultadoAuth.CREDENCIALES_INVALIDAS -> "Correo o contraseña incorrectos."
                    }
                )
            }
            if (resultado == ResultadoAuth.OK) onExito(repo.perfilCompleto())
        }
    }
}
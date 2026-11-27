package com.example.pequenossabores.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pequenossabores.data.repository.ChatRepository
import com.example.pequenossabores.data.repository.MensajeChat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    private val repository: ChatRepository = ChatRepository()
) : ViewModel() {

    companion object {
        private const val TAG = "ChatViewModel"
    }

    private val _mensajes = MutableStateFlow<List<MensajeChat>>(emptyList())
    val mensajes = _mensajes.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando = _cargando.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun enviarMensaje(texto: String) {
        val mensaje = texto.trim()

        if (mensaje.isEmpty() || _cargando.value) {
            Log.w(TAG, "Envío omitido: mensaje vacío o petición en curso")
            return
        }

        Log.e(TAG, "Se solicitó enviar un mensaje: ${mensaje.take(50)}")

        val historialAnterior = _mensajes.value
        _error.value = null

        _mensajes.value = historialAnterior + MensajeChat(
            texto = mensaje,
            esAsistente = false
        )

        _cargando.value = true

        viewModelScope.launch {
            try {
                Log.e(TAG, "Llamando a ChatRepository")

                val respuesta = repository.enviarMensaje(
                    mensaje = mensaje,
                    historial = historialAnterior
                )

                Log.e(TAG, "Respuesta recibida correctamente")

                _mensajes.value = _mensajes.value + MensajeChat(
                    texto = respuesta,
                    esAsistente = true
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error enviando mensaje a PequeChef", e)

                _error.value = e.message
                    ?: "No fue posible conectar con PequeChef."
            } finally {
                _cargando.value = false
                Log.e(TAG, "Proceso de envío finalizado")
            }
        }
    }

    fun limpiarError() {
        _error.value = null
    }
}
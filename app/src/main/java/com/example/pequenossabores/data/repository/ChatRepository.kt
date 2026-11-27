package com.example.pequenossabores.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import android.util.Log

data class MensajeChat(
    val texto: String,
    val esAsistente: Boolean
)

class ChatRepository {

    companion object {
        private const val URL_CHAT = "http://127.0.0.1:8000/chat.php"
    }

    suspend fun enviarMensaje(
        mensaje: String,
        historial: List<MensajeChat>
    ): String = withContext(Dispatchers.IO) {

        Log.e("ChatRepository", "Intentando conectar con: $URL_CHAT")

        val conexion = URL(URL_CHAT).openConnection()
                as HttpURLConnection

        try {
            conexion.requestMethod = "POST"
            conexion.connectTimeout = 15000
            conexion.readTimeout = 70000
            conexion.doOutput = true

            conexion.setRequestProperty(
                "Content-Type",
                "application/json; charset=UTF-8"
            )

            val historialJson = JSONArray()

            historial.takeLast(10).forEach { item ->
                historialJson.put(
                    JSONObject().apply {
                        put(
                            "rol",
                            if (item.esAsistente) "model" else "user"
                        )
                        put("texto", item.texto)
                    }
                )
            }

            val cuerpo = JSONObject().apply {
                put("mensaje", mensaje)
                put("historial", historialJson)
            }

            OutputStreamWriter(
                conexion.outputStream,
                Charsets.UTF_8
            ).use { writer ->
                writer.write(cuerpo.toString())
            }

            val codigo = conexion.responseCode

            val flujo = if (codigo in 200..299) {
                conexion.inputStream
            } else {
                conexion.errorStream
            }

            val respuestaServidor = flujo?.let { stream ->
                BufferedReader(
                    InputStreamReader(stream, Charsets.UTF_8)
                ).use { reader ->
                    reader.readText()
                }
            }.orEmpty()

            val json = try {
                JSONObject(respuestaServidor)
            } catch (_: Exception) {
                JSONObject()
            }

            if (codigo !in 200..299) {
                throw Exception(
                    json.optString(
                        "error",
                        "Error al comunicarse con PequeChef."
                    )
                )
            }

            json.optString("respuesta").takeIf {
                it.isNotBlank()
            } ?: throw Exception(
                "El servidor no devolvió una respuesta válida."
            )

        } catch (e: Exception) {
            Log.e("ChatRepository", "Falló la solicitud a PHP", e)
            throw e
        } finally {
            conexion.disconnect()
        }
    }
}

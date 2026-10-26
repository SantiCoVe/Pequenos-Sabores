package com.example.pequenossabores.data.repository

import android.content.Context

class SesionPreferences(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences("mi_configuracion", Context.MODE_PRIVATE)

    fun guardarSesion(correo: String) {
        prefs.edit()
            .putBoolean("sesion_activa", true)
            .putString("correo_activo", correo)
            .apply()
    }

    fun haySesion(): Boolean = prefs.getBoolean("sesion_activa", false)

    fun correoActivo(): String? = prefs.getString("correo_activo", null)

    fun setPerfilCompleto(completo: Boolean) {
        prefs.edit().putBoolean("perfil_completo", completo).apply()
    }

    fun perfilCompleto(): Boolean = prefs.getBoolean("perfil_completo", false)

    fun cerrarSesion() {
        prefs.edit()
            .remove("sesion_activa")
            .remove("correo_activo")
            .remove("perfil_completo")
            .apply()
    }
}
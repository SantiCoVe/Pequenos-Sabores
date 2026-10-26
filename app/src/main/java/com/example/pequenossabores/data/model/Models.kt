package com.example.pequenossabores.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class RolFamiliar(val etiqueta: String, val emoji: String) {
    MAMA("Mamá", "🌸"), PAPA("Papá", "🌱"), CUIDADOR("Cuidador/a", "🧸"), ABUELO("Abuelo/a", "🏡")
}

/** Tabla "usuarios" en Room. El correo es la clave primaria (no se repite). */
@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey
    val correo: String,
    val nombre: String,
    val contrasenaHash: String,
    val rol: String            // guarda RolFamiliar.name
)

enum class ResultadoAuth { OK, CORREO_EXISTENTE, CREDENCIALES_INVALIDAS }

data class MenuDelDia(
    val titulo: String,
    val momento: String,
    val tiempoMin: Int,
    val textura: String,
    val kcal: Int
)

data class ProgresoGrupo(val nombre: String, val emoji: String, val porcentaje: Int)
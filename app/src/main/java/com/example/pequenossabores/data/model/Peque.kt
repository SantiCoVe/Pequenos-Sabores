package com.example.pequenossabores.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Un peque por cuenta: el correo del usuario es la clave primaria. */
@Entity(tableName = "peques")
data class PequeEntity(
    @PrimaryKey val correoUsuario: String,
    val nombre: String,
    val avatar: Int,
    val edadMeses: Int,
    val preferencias: String,
    val noLeGustan: String,
    val alergias: String
) {
    val edadTexto: String get() = if (edadMeses >= 24) "${edadMeses / 12} años" else "$edadMeses meses"

    fun preferenciasSet(): Set<String> = preferencias.split("|").filter { it.isNotBlank() }.toSet()
    fun alergiasSet(): Set<String> = alergias.split("|").filter { it.isNotBlank() }.toSet()
}

@Entity(tableName = "favoritos", primaryKeys = ["correoUsuario", "recetaId"])
data class FavoritoEntity(
    val correoUsuario: String,
    val recetaId: Int
)
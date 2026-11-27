package com.example.pequenossabores.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "consumos",
    indices = [Index(value = ["correoUsuario", "fecha"])]
)
data class ConsumoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val correoUsuario: String,
    val recetaId: Int,
    val nombreReceta: String,
    val fecha: String,
    val gruposConsumidos: String
)
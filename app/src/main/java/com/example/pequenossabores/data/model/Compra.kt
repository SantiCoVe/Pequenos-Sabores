package com.example.pequenossabores.data.model

data class ItemCompra(
    val id: Int,
    val nombre: String,
    val cantidad: String,
    val categoria: String,
    val comprado: Int          // 0 o 1, como lo devuelve MySQL
)

data class ItemEnvio(val nombre: String, val cantidad: String, val categoria: String)

data class RespuestaServicio(
    val mensaje: String,
    val id: Int = 0,
    val insertados: Int = 0
)
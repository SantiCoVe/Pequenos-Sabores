package com.example.pequenossabores.data.repository

import com.example.pequenossabores.data.model.ItemCompra
import com.example.pequenossabores.data.model.ItemEnvio
import com.example.pequenossabores.data.remote.RetrofitClient
import com.example.pequenossabores.data.remote.ServicioApi
import com.google.gson.Gson

class ComprasRepository(private val api: ServicioApi = RetrofitClient.api) {

    suspend fun listar(correo: String): List<ItemCompra> = api.listar(correo)

    suspend fun agregar(correo: String, nombre: String, cantidad: String, categoria: String): Boolean =
        api.registrar(correo, nombre, cantidad, categoria).mensaje == "OK"

    suspend fun agregarLote(correo: String, items: List<ItemEnvio>): Boolean =
        api.registrarLote(correo, Gson().toJson(items)).mensaje == "OK"

    suspend fun marcar(correo: String, id: Int, comprado: Boolean): Boolean =
        api.marcar(correo, id, if (comprado) 1 else 0).mensaje == "OK"

    suspend fun vaciarMarcados(correo: String): Boolean =
        api.eliminarMarcados(correo).mensaje == "OK"
}
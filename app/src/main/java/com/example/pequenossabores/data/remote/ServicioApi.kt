package com.example.pequenossabores.data.remote

import com.example.pequenossabores.data.model.RespuestaServicio
import com.example.pequenossabores.data.model.ItemCompra
import retrofit2.http.*

interface ServicioApi {

    @GET("pequenos_sabores/listar.php")
    suspend fun listar(@Query("correo") correo: String): List<ItemCompra>

    @FormUrlEncoded
    @POST("pequenos_sabores/registrar.php")
    suspend fun registrar(
        @Field("correo") correo: String,
        @Field("nombre") nombre: String,
        @Field("cantidad") cantidad: String,
        @Field("categoria") categoria: String
    ): RespuestaServicio

    @FormUrlEncoded
    @POST("pequenos_sabores/registrar_lote.php")
    suspend fun registrarLote(
        @Field("correo") correo: String,
        @Field("items") itemsJson: String
    ): RespuestaServicio

    @FormUrlEncoded
    @POST("pequenos_sabores/actualizar.php")
    suspend fun marcar(
        @Field("correo") correo: String,
        @Field("id") id: Int,
        @Field("comprado") comprado: Int
    ): RespuestaServicio

    @FormUrlEncoded
    @POST("pequenos_sabores/eliminar.php")
    suspend fun eliminarMarcados(@Field("correo") correo: String): RespuestaServicio
}
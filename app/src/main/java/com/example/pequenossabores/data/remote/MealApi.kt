package com.example.pequenossabores.data.remote

import com.example.pequenossabores.data.model.MealsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {

    @GET("search.php")
    suspend fun buscar(@Query("s") texto: String): MealsResponse

    @GET("lookup.php")
    suspend fun detalle(@Query("i") id: String): MealsResponse
}
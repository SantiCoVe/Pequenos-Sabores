package com.example.pequenossabores.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/** Cliente hacia TU servidor XAMPP (guía de servicios con PHP). */
object RetrofitClient {

    // IP de tu computador en la red (ipconfig / ipconfig getifaddr en0).
    // Si usas el emulador de Android Studio también sirve "http://10.0.2.2/"
    private const val BASE_URL = "http://192.168.56.1/"

    private val http = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(8, TimeUnit.SECONDS)
        .build()

    val api: ServicioApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(http)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ServicioApi::class.java)
    }
}
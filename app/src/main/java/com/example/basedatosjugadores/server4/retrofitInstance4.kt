package com.example.basedatosjugadores.server4

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance4 {
    private const val BASE_URL = "https://lanusstatsserver4-production.up.railway.app/" // Cambia por tu URL de Railway

    val api: ApiService4 by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService4::class.java)
    }
}
package com.example.basedatosjugadores.server5

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance5 {
    private const val BASE_URL = "https://lanusstatsserver5-production.up.railway.app/" // Cambia por tu URL de Railway

    val api: ApiService5 by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService5::class.java)
    }
}
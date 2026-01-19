package com.example.basedatosjugadores.server7

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance7 {
    private const val BASE_URL = "https://lanusstatsserver7-production.up.railway.app/" // Cambia por tu URL de Railway

    val api: ApiService7 by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService7::class.java)
    }
}
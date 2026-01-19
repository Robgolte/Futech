package com.example.basedatosjugadores.server2

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance2 {
    private const val BASE_URL = "https://lanusstatsserver2-production.up.railway.app/" // Cambia por tu URL de Railway

    val api: ApiService2 by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService2::class.java)
    }
}

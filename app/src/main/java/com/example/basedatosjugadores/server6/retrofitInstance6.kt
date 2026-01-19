package com.example.basedatosjugadores.server6

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance6 {
    private const val BASE_URL = "https://lanusstatsserver6-production.up.railway.app/" // Cambia por tu URL de Railway

    val api: ApiService6 by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService6::class.java)
    }
}
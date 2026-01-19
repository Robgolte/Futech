package com.example.basedatosjugadores.server3

import com.example.basedatosjugadores.server2.ApiService2
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance3 {
    private const val BASE_URL = "https://lanusstatsserver3-production.up.railway.app/" // Cambia por tu URL de Railway

    val api: ApiService3 by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService3::class.java)
    }
}
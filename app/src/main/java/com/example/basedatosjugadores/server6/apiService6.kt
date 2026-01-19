package com.example.basedatosjugadores.server6

import retrofit2.http.GET

interface ApiService6 {
    @GET("/tabla")
    suspend fun obtenerTabla(): List<Equipo6>
}
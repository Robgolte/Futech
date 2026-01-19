package com.example.basedatosjugadores.server4

import retrofit2.http.GET

interface ApiService4 {
    @GET("/tabla")
    suspend fun obtenerTabla(): List<Equipo4>
}
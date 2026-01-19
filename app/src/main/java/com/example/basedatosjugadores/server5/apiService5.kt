package com.example.basedatosjugadores.server5

import retrofit2.http.GET

interface ApiService5 {
    @GET("/tabla")
    suspend fun obtenerTabla(): List<Equipo5>
}
package com.example.basedatosjugadores.server3

import retrofit2.http.GET

interface ApiService3 {
    @GET("/tabla")
    suspend fun obtenerTabla(): List<Equipo3>
}
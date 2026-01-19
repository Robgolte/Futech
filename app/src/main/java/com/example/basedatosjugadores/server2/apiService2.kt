package com.example.basedatosjugadores.server2

import retrofit2.http.GET

interface ApiService2 {
    @GET("/tabla")
    suspend fun obtenerTabla(): List<Equipo2>
}

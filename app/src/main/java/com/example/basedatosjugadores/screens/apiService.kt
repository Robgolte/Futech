package com.example.basedatosjugadores.screens

import Equipo
import retrofit2.http.GET

interface ApiService {
    @GET("/tabla")
    suspend fun obtenerTabla(): List<Equipo>
}

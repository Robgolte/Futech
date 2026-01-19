package com.example.basedatosjugadores.server7

import com.example.basedatosjugadores.server6.Equipo6
import retrofit2.http.GET

interface ApiService7 {
    @GET("/tabla")
    suspend fun obtenerTabla(): List<Equipo7>
}
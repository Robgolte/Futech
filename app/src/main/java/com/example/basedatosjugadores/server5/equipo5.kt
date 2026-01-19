package com.example.basedatosjugadores.server5

import com.google.gson.annotations.SerializedName

data class Equipo5(
    @SerializedName("RL") val RL: Int,  // Cambiado de "Rk" a "RL"
    @SerializedName("Equipo") val Equipo: String,  // Cambiado de "Squad" a "Equipo"
    @SerializedName("PJ") val MP: Int,  // Cambiado de "MP" a "PJ"
    @SerializedName("PG") val W: Int,  // Cambiado de "W" a "PG"
    @SerializedName("PE") val D: Int,  // Cambiado de "D" a "PE"
    @SerializedName("PP") val L: Int,  // Cambiado de "L" a "PP"
    @SerializedName("GF") val GF: Int,
    @SerializedName("GC") val GC: Int,
    @SerializedName("DG") val DG: Int,  // Cambiado de "GD" a "DG"
    @SerializedName("Pts") val Pts: Int,
    @SerializedName("Pts/PJ") val PtsPerMP: Double,  // Cambiado de "Pts/MP" a "Pts/PJ"
    @SerializedName("xG") val xG: Double,
    @SerializedName("xGA") val xGA: Double,
    @SerializedName("xGD") val xGD: Double,
    @SerializedName("xGD/90") val xGDPer90: Double,
    @SerializedName("Últimos 5") val Last5: String,  // Cambiado de "Last 5" a "Últimos 5"
    @SerializedName("Asistencia") val Attendance: Int?,  // Cambiado de "Attendance" a "Asistencia"
    @SerializedName("Máximo Goleador del Equipo") val TopTeamScorer: String,  // Cambiado de "Top Team Scorer" a "Máximo Goleador del Equipo"
    @SerializedName("Portero") val Goalkeeper: String,  // Cambiado de "Goalkeeper" a "Portero"
    @SerializedName("Notas") val Notes: String?  // Esto debería manejarse como nullable
)
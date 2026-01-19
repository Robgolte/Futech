package com.example.basedatosjugadores.screens.estadisticas

data class Jugador(
    val index: Int,
    val player: String,
    val nation: String,
    val posRaw: String,
    val mainPos: String,
    val altPos: String?,
    val Squad: String,
    val comp: String,
    val born: String,
    val mp: Int,
    val starts: Int,
    val min: Int,
    val gls: Int,   // goles reales (se usará para calcular G+A)
    val ast: Int,   // asistencias reales
    val xg: Double, // goles esperados (se lee de la columna 32)
    val xAG: Double // asistencias esperadas (se lee de la columna 33)
)
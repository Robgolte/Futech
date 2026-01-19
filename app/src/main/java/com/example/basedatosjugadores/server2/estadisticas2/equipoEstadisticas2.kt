package com.example.basedatosjugadores.server2.estadisticas2

data class EquipoEstadisticas2(
    val Squad: String,
    val sca: Int,
    val passLive: Int,
    val sh: Int,
    val sca90: Double,
    val passDead: Int,
    val to: Int,
    val fld: Int,
    val def: Int,
    val gca: Int,
    val gca90: Double
)
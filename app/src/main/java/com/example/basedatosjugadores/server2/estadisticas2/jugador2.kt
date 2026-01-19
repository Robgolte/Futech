package com.example.basedatosjugadores.server2.estadisticas2

/**
 * Esta clase representa a un jugador usando el CSV con la siguiente estructura:
 *
 * ,Player,Nation,Pos,Squad,Comp,Age,Born,MP,Starts,Min,90s,Gls,Ast,G+A,G-PK,PK,PKatt,CrdY,CrdR,xG,npxG,xAG,npxG+xAG,PrgC,PrgP,PrgR,Gls,Ast,G+A,G-PK,G+A-PK,xG,xAG,xG+xAG,npxG,npxG+xAG
 *
 * Se toman los atributos básicos:
 * - [index]: Índice del registro
 * - [player]: Nombre del jugador
 * - [nation]: Nacionalidad (se extrae el primer valor y se pasa a minúsculas)
 * - [posRaw]: Posición original (posiblemente con varias opciones separadas por coma)
 * - [mainPos]: Posición principal
 * - [altPos]: Posición alternativa (si existe)
 * - [Squad]: Equipo al que pertenece
 * - [comp]: Competición (ej. "La Liga")
 * - [born]: Año de nacimiento u otra info (según CSV)
 * - [mp]: Partidos jugados (MP)
 * - [starts]: Partidos iniciados
 * - [min]: Minutos jugados
 * - [gls]: Goles (columna 12)
 * - [ast]: Asistencias (columna 13)
 * - [xg]: Goles esperados (se lee de la columna 32)
 * - [xAG]: Asistencias esperadas (se lee de la columna 33)
 */

data class Jugador2(
    val id: Int,
    val player: String,
    val gls: Int,
    val ast: Int,
    val xg: Double,
    val xAG: Double,
    val rating: Double,
    val nation: String,
    val mainPos: String,
    val altPos: String?,
    val Squad: String,
    val born: String,
    /** Indica si el jugador está marcado como favorito */
    var isFavorite: Boolean = false
)

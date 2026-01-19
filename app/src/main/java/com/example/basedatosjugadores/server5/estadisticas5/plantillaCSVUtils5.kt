package com.example.basedatosjugadores.server5.estadisticas5

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Esta función carga los datos del CSV de jugadores (por ejemplo, "jugadores_liga.csv")
 * y mapea cada línea a un objeto [Jugador5].
 *
 * Se asume que el CSV tiene la siguiente estructura:
 *
 * ,Player,Nation,Pos,Squad,Comp,Age,Born,MP,Starts,Min,90s,Gls,Ast,G+A, ... , xG, xAG, ...
 *
 * Se leen [xg] y [xAG] de las columnas 32 y 33 respectivamente.
 */
suspend fun cargarPlantillaDesdeCSV5(context: Context): List<Jugador5> {
    val jugadorList = mutableListOf<Jugador5>()
    try {
        // Asegúrate de que el archivo "jugadores_liga.csv" esté en la carpeta assets
        val inputStream = context.assets.open("Ligue_1_stats_2025_02_21.csv")
        val reader = BufferedReader(InputStreamReader(inputStream))
        reader.readLine() // Saltar la cabecera
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            try {
                val data = line!!.split(",").map { it.trim() }
                val idx = data[0].toIntOrNull() ?: 0
                val player = data[1]
                // Extraer solo el primer valor de la columna Nation y pasarlo a minúsculas
                val nation = data[2].split(" ").first().lowercase()
                val posRaw = data[3]
                val posParts = posRaw.split(",").map { it.trim() }
                val mainPos = posParts[0]
                val altPos = if (posParts.size > 1) posParts[1] else null
                val Squad = data[4]
                val comp = data[5]
                val born = data[7]
                val mp = data[8].toIntOrNull() ?: 0
                val starts = data[9].toIntOrNull() ?: 0
                val min = data[10].toIntOrNull() ?: 0
                val gls = data[12].toIntOrNull() ?: 0
                val ast = data[13].toIntOrNull() ?: 0
                // Se leen xg y xAG de las columnas 32 y 33 (recordar que el CSV tiene 37 columnas en total)
                val xg = data.getOrNull(32)?.toDoubleOrNull() ?: 0.0
                val xAG = data.getOrNull(33)?.toDoubleOrNull() ?: 0.0

                val jugador = Jugador5(
                    index = idx,
                    player = player,
                    nation = nation,
                    posRaw = posRaw,
                    mainPos = mainPos,
                    altPos = altPos,
                    Squad = Squad,
                    comp = comp,
                    born = born,
                    mp = mp,
                    starts = starts,
                    min = min,
                    gls = gls,
                    ast = ast,
                    xg = xg,
                    xAG = xAG
                )
                jugadorList.add(jugador)
            } catch (e: Exception) {
                Log.e("CSV_ERROR", "Error processing line: $line", e)
            }
        }
    } catch (e: Exception) {
        Log.e("CSV_ERROR", "General error reading CSV", e)
    }
    return jugadorList
}
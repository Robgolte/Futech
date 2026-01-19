package com.example.basedatosjugadores.server2.estadisticas2

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader

suspend fun cargarPlantillaDesdeCSV2(context: Context): List<Jugador2> {
    val jugadorList = mutableListOf<Jugador2>()
    try {
        val inputStream = context.assets.open("estadisticas_actualizadas_LaLiga.csv")
        val reader = BufferedReader(InputStreamReader(inputStream))
        reader.readLine() // Saltar la cabecera

        var line: String?
        while (reader.readLine().also { line = it } != null) {
            try {
                val data = line!!.split(",").map { it.trim() }
                if (data.size < 12) continue // evitar líneas incompletas

                val player = data[0]
                val id = data[2].toIntOrNull() ?: 0
                val ast = data[3].toDoubleOrNull()?.toInt() ?: 0
                val gls = data[4].toDoubleOrNull()?.toInt() ?: 0
                val xg = data[5].toDoubleOrNull() ?: 0.0
                val xAG = data[6].toDoubleOrNull() ?: 0.0
                val rating = data[7].toDoubleOrNull() ?: 0.0
                val nation = data[8].split(" ").first().lowercase()
                val posRaw = data[9]
                val posParts = posRaw.split(",").map { it.trim() }
                val mainPos = posParts[0]
                val altPos = if (posParts.size > 1) posParts[1] else null
                val Squad = data[10]
                val born = data[11]

                val jugador = Jugador2(
                    id = id,
                    player = player,
                    gls = gls,
                    ast = ast,
                    xg = xg,
                    xAG = xAG,
                    rating = rating,
                    nation = nation,
                    mainPos = mainPos,
                    altPos = altPos,
                    Squad = Squad,
                    born = born
                )
                jugadorList.add(jugador)
            } catch (e: Exception) {
                Log.e("CSV_ERROR", "Error processing line: $line", e)
            }
        }
    } catch (e: Exception) {
        Log.e("CSV_ERROR", "Error reading CSV file", e)
    }

    return jugadorList
}

package com.example.basedatosjugadores.screens.estadisticas

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

suspend fun cargarPlantillaDesdeCSV(context: Context): List<Jugador> = withContext(Dispatchers.IO) {
    val jugadorList = mutableListOf<Jugador>()
    try {
        // Asegúrate de que el archivo "plantilla_equipo.csv" se encuentre en assets
        val inputStream = context.assets.open("Premier League_stats_2025_02_12.csv")
        val reader = BufferedReader(InputStreamReader(inputStream))
        reader.readLine() // Saltar la cabecera
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            try {
                val data = line!!.split(",").map { it.trim() }
                val idx = data[0].toIntOrNull() ?: 0
                val player = data[1]
                // Modificación: Extraer solo el primer valor de la columna Nation y pasarlo a minúsculas
                val nation = data[2].split(" ").first().lowercase()
                val posRaw = data[3]
                val posParts = posRaw.split(",").map { it.trim() }
                val mainPos = posParts[0]
                val altPos = if (posParts.size > 1) posParts[1] else null
                // Se asigna la columna correspondiente a "Squad" (con "S" mayúscula)
                val Squad = data[4]
                val comp = data[5]
                val born = data[7]
                val mp = data[8].toIntOrNull() ?: 0
                val starts = data[9].toIntOrNull() ?: 0
                val min = data[10].toIntOrNull() ?: 0
                val gls = data[12].toIntOrNull() ?: 0
                val ast = data[13].toIntOrNull() ?: 0
                // Se leen xg y xAG, por ejemplo, de las columnas 32 y 33
                val xg = data.getOrNull(32)?.toDoubleOrNull() ?: 0.0
                val xAG = data.getOrNull(33)?.toDoubleOrNull() ?: 0.0

                val jugador = Jugador(
                    index = idx,
                    player = player,
                    nation = nation, // Ahora solo contiene el primer valor en minúsculas
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
                Log.e("CSV_ERROR", "Error procesando la línea: $line", e)
            }
        }
    } catch (e: Exception) {
        Log.e("CSV_ERROR", "Error general al leer CSV", e)
    }
    jugadorList
}
package com.example.basedatosjugadores.server5.estadisticas5

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader


fun cargarDatosDesdeCSV(context: Context): List<EquipoEstadisticas5> {
    val equipoList = mutableListOf<EquipoEstadisticas5>()
    try {
        // Se asume que el archivo se llama "estadisticas_equipos.csv" y está en assets
        val inputStream = context.assets.open("estadisticas_equipo_Ligue1.csv")
        val reader = BufferedReader(InputStreamReader(inputStream))
        reader.readLine()  // Saltar la cabecera
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            try {
                val data = line!!.split(",").map { it.trim() }
                val equipo = EquipoEstadisticas5(
                    Squad = data[1].replace("_", " "),
                    sca = data[4].toIntOrNull() ?: 0,
                    sca90 = data[5].toDoubleOrNull() ?: 0.0,
                    passLive = data[6].toIntOrNull() ?: 0,
                    passDead = data[7].toIntOrNull() ?: 0,
                    to = data[8].toIntOrNull() ?: 0,
                    sh = data[9].toIntOrNull() ?: 0,
                    fld = data[10].toIntOrNull() ?: 0,
                    def = data[11].toIntOrNull() ?: 0,
                    gca = data[12].toIntOrNull() ?: 0,
                    gca90 = data[13].toDoubleOrNull() ?: 0.0
                )
                equipoList.add(equipo)
            } catch (e: Exception) {
                Log.e("CSV_ERROR", "Error processing line: $line", e)
            }
        }
    } catch (e: Exception) {
        Log.e("CSV_ERROR", "General error reading CSV", e)
    }
    return equipoList
}
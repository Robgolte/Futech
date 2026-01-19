package com.example.basedatosjugadores.screens.estadisticas

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader

fun cargarDatosDesdeCSV(context: Context): List<EquipoEstadisticas> {
    val equipoList = mutableListOf<EquipoEstadisticas>()
    try {
        val inputStream = context.assets.open("estadisticas_equipo_Premier.csv")
        val reader = BufferedReader(InputStreamReader(inputStream))
        reader.readLine()  // Saltar cabecera

        var line: String?
        while (reader.readLine().also { line = it } != null) {
            try {
                val data = line!!.split(",").map { it.trim() }
                val equipo = EquipoEstadisticas(
                    Squad = data[1].replace("_", " "), // Normalizar nombres
                    sca = data[4].toInt(),
                    passLive = data[6].toInt(),
                    sh = data[9].toInt(),
                    sca90 = data[5].toDouble(),
                    passDead = data[7].toInt(),
                    to = data[8].toInt(),
                    fld = data[10].toInt(),
                    def = data[11].toInt(),
                    gca = data[12].toInt(),
                    gca90 = data[13].toDouble()
                )
                equipoList.add(equipo)
            } catch (e: Exception) {
                Log.e("CSV_ERROR", "Error procesando línea: $line", e)
            }
        }
    } catch (e: Exception) {
        Log.e("CSV_ERROR", "Error general al leer CSV", e)
    }
    return equipoList
}
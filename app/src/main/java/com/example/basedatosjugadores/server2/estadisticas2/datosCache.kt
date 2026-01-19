package com.example.basedatosjugadores.server2.estadisticas2

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.io.FileInputStream
import java.io.InputStreamReader

// Guardar datos en caché
fun guardarDatosEnCache(context: Context, playerId: String, datosJson: String) {
    try {
        // Generar el nombre del archivo con el ID del jugador
        val cacheFile = File(context.cacheDir, "shotmap_$playerId.json")

        // Escribir los datos en el archivo
        val fileOutputStream = FileOutputStream(cacheFile)
        val writer = OutputStreamWriter(fileOutputStream)
        writer.write(datosJson)
        writer.close()

        // Mostrar mensaje cuando los datos se guarden correctamente
        Log.d("Caché", "Datos guardados en caché para el jugador $playerId.")

    } catch (e: Exception) {
        e.printStackTrace()
    }
}

// Obtener los datos del caché
fun obtenerDatosDesdeCache(context: Context, playerId: String): String? {
    try {
        // Buscar el archivo con el ID del jugador
        val cacheFile = File(context.cacheDir, "shotmap_$playerId.json")
        if (cacheFile.exists()) {
            val fileInputStream = FileInputStream(cacheFile)
            val reader = InputStreamReader(fileInputStream)
            val stringBuilder = StringBuilder()

            var char: Int
            while (reader.read().also { char = it } != -1) {
                stringBuilder.append(char.toChar())
            }
            reader.close()

            return stringBuilder.toString()  // Devuelve el JSON leído
        } else {
            return null  // Si no existe el archivo en caché
        }

    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

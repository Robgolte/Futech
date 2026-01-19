package com.example.basedatosjugadores.server3

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basedatosjugadores.server2.RetrofitInstance2
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

class TablaViewModel3(private val context: Context) : ViewModel() {

    private val _tabla = MutableStateFlow<ResultState3<List<Equipo3>>>(ResultState3.Loading)
    val tabla: StateFlow<ResultState3<List<Equipo3>>> get() = _tabla

    init {
        obtenerTabla()
    }

    private fun obtenerTabla() {
        viewModelScope.launch {
            try {
                _tabla.value = ResultState3.Loading
                // Intentamos obtener la tabla desde el servidor
                val response = RetrofitInstance3.api.obtenerTabla()
                _tabla.value = ResultState3.Success(response)
            } catch (e: Exception) {
                // Si falla, intentamos cargar los datos locales
                _tabla.value = cargarTablaLocal()
            }
        }
    }

    private fun cargarTablaLocal(): ResultState3<List<Equipo3>> {
        return try {
            // Abrir archivo JSON local desde la carpeta `assets`
            val inputStream = context.assets.open("tabla3.json")
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val json = bufferedReader.use { it.readText() }

            // Parsear el JSON a una lista de objetos Equipo3
            val equipoType = object : TypeToken<List<Equipo3>>() {}.type
            val equipos: List<Equipo3> = Gson().fromJson(json, equipoType)

            // Devolver el resultado como Success
            ResultState3.Success(equipos)
        } catch (e: Exception) {
            // Si ocurre un error, devolvemos un estado de Error
            ResultState3.Error("Error al cargar datos locales: ${e.message}")
        }
    }
}

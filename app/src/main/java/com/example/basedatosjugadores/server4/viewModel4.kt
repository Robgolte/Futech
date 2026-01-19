package com.example.basedatosjugadores.server4

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

class TablaViewModel4(private val context: Context) : ViewModel() {

    private val _tabla = MutableStateFlow<ResultState4<List<Equipo4>>>(ResultState4.Loading)
    val tabla: StateFlow<ResultState4<List<Equipo4>>> get() = _tabla

    init {
        obtenerTabla()
    }

    private fun obtenerTabla() {
        viewModelScope.launch {
            try {
                _tabla.value = ResultState4.Loading
                // Intentamos obtener la tabla desde el servidor
                val response = RetrofitInstance4.api.obtenerTabla()
                _tabla.value = ResultState4.Success(response)
            } catch (e: Exception) {
                // Si falla, intentamos cargar los datos locales
                _tabla.value = cargarTablaLocal()
            }
        }
    }

    private fun cargarTablaLocal(): ResultState4<List<Equipo4>> {
        return try {
            // Abrir archivo JSON local desde la carpeta `assets`
            val inputStream = context.assets.open("tabla4.json")
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val json = bufferedReader.use { it.readText() }

            // Parsear el JSON a una lista de objetos Equipo4
            val equipoType = object : TypeToken<List<Equipo4>>() {}.type
            val equipos: List<Equipo4> = Gson().fromJson(json, equipoType)

            // Devolver el resultado como Success
            ResultState4.Success(equipos)
        } catch (e: Exception) {
            // Si ocurre un error, devolvemos un estado de Error
            ResultState4.Error("Error al cargar datos locales: ${e.message}")
        }
    }
}

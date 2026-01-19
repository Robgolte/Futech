package com.example.basedatosjugadores.server6

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

class TablaViewModel6(private val context: Context) : ViewModel() {

    private val _tabla = MutableStateFlow<ResultState6<List<Equipo6>>>(ResultState6.Loading)
    val tabla: StateFlow<ResultState6<List<Equipo6>>> get() = _tabla

    init {
        obtenerTabla()
    }

    private fun obtenerTabla() {
        viewModelScope.launch {
            try {
                _tabla.value = ResultState6.Loading
                // Intentamos obtener la tabla desde el servidor
                val response = RetrofitInstance6.api.obtenerTabla()
                _tabla.value = ResultState6.Success(response)
            } catch (e: Exception) {
                // Si falla, intentamos cargar los datos locales
                _tabla.value = cargarTablaLocal()
            }
        }
    }

    private fun cargarTablaLocal(): ResultState6<List<Equipo6>> {
        return try {
            // Abrir archivo JSON local desde la carpeta `assets`
            val inputStream = context.assets.open("tabla6.json")
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val json = bufferedReader.use { it.readText() }

            // Parsear el JSON a una lista de objetos Equipo6
            val equipoType = object : TypeToken<List<Equipo6>>() {}.type
            val equipos: List<Equipo6> = Gson().fromJson(json, equipoType)

            // Devolver el resultado como Success
            ResultState6.Success(equipos)
        } catch (e: Exception) {
            // Si ocurre un error, devolvemos un estado de Error
            ResultState6.Error("Error al cargar datos locales: ${e.message}")
        }
    }
}

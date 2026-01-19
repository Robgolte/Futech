package com.example.basedatosjugadores.server5

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

class TablaViewModel5(private val context: Context) : ViewModel() {

    private val _tabla = MutableStateFlow<ResultState5<List<Equipo5>>>(ResultState5.Loading)
    val tabla: StateFlow<ResultState5<List<Equipo5>>> get() = _tabla

    init {
        obtenerTabla()
    }

    private fun obtenerTabla() {
        viewModelScope.launch {
            try {
                _tabla.value = ResultState5.Loading
                // Intentamos obtener la tabla desde el servidor
                val response = RetrofitInstance5.api.obtenerTabla()
                _tabla.value = ResultState5.Success(response)
            } catch (e: Exception) {
                // Si falla, intentamos cargar los datos locales
                _tabla.value = cargarTablaLocal()
            }
        }
    }

    private fun cargarTablaLocal(): ResultState5<List<Equipo5>> {
        return try {
            // Abrir archivo JSON local desde la carpeta `assets`
            val inputStream = context.assets.open("tabla5.json")
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val json = bufferedReader.use { it.readText() }

            // Parsear el JSON a una lista de objetos Equipo5
            val equipoType = object : TypeToken<List<Equipo5>>() {}.type
            val equipos: List<Equipo5> = Gson().fromJson(json, equipoType)

            // Devolver el resultado como Success
            ResultState5.Success(equipos)
        } catch (e: Exception) {
            // Si ocurre un error, devolvemos un estado de Error
            ResultState5.Error("Error al cargar datos locales: ${e.message}")
        }
    }
}

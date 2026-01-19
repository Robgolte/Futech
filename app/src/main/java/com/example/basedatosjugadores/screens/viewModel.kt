package com.example.basedatosjugadores.screens

import Equipo
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

class TablaViewModel(private val context: Context) : ViewModel() {

    private val _tabla = MutableStateFlow<ResultState<List<Equipo>>>(ResultState.Loading)
    val tabla: StateFlow<ResultState<List<Equipo>>> get() = _tabla

    init {
        obtenerTabla()
    }

    private fun obtenerTabla() {
        viewModelScope.launch {
            try {
                _tabla.value = ResultState.Loading
                // Intentamos obtener la tabla del servidor
                val response = RetrofitInstance.api.obtenerTabla()
                _tabla.value = ResultState.Success(response)
            } catch (e: Exception) {
                // Si falla, cargamos el JSON local
                _tabla.value = cargarTablaLocal()
            }
        }
    }

    // Función para cargar datos desde un archivo JSON local
    private fun cargarTablaLocal(): ResultState<List<Equipo>> {
        return try {
            val inputStream = context.assets.open("tabla.json") // Archivo en la carpeta `assets`
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val json = bufferedReader.use { it.readText() }
            val equipoType = object : TypeToken<List<Equipo>>() {}.type
            val equipos: List<Equipo> = Gson().fromJson(json, equipoType)
            ResultState.Success(equipos) // Devuelve los datos del JSON local
        } catch (e: Exception) {
            ResultState.Error("Error al cargar datos locales: ${e.message}")
        }
    }
}

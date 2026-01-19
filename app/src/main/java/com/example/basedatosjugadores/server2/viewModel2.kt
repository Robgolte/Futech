package com.example.basedatosjugadores.server2

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

class TablaViewModel2(private val context: Context) : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    private val _tabla = MutableStateFlow<ResultState2<List<Equipo2>>>(ResultState2.Loading)
    val tabla: StateFlow<ResultState2<List<Equipo2>>> get() = _tabla

    private val _favoritos = MutableStateFlow<Set<String>>(emptySet())  // Almacenamos los equipos favoritos
    val favoritos: StateFlow<Set<String>> get() = _favoritos

    init {
        obtenerTabla()
        obtenerFavoritos()
    }

    private fun obtenerTabla() {
        viewModelScope.launch {
            try {
                _tabla.value = ResultState2.Loading
                // Intentamos obtener la tabla del servidor
                val response = RetrofitInstance2.api.obtenerTabla()
                _tabla.value = ResultState2.Success(response)
            } catch (e: Exception) {
                // Si falla, cargamos el JSON local
                _tabla.value = cargarTablaLocal()
            }
        }
    }

    // Función para cargar datos desde un archivo JSON local
    private fun cargarTablaLocal(): ResultState2<List<Equipo2>> {
        return try {
            val inputStream = context.assets.open("tabla2.json") // Archivo en la carpeta `assets`
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val json = bufferedReader.use { it.readText() }
            val equipoType = object : TypeToken<List<Equipo2>>() {}.type
            val equipos: List<Equipo2> = Gson().fromJson(json, equipoType)
            ResultState2.Success(equipos) // Devuelve los datos del JSON local
        } catch (e: Exception) {
            ResultState2.Error("Error al cargar datos locales: ${e.message}")
        }
    }

    // Función para obtener los favoritos del usuario actual
    private fun obtenerFavoritos() {
        val user = auth.currentUser
        if (user != null) {
            db.collection("users")
                .document(user.uid)
                .collection("favorites")
                .addSnapshotListener { snapshot, _ ->
                    if (snapshot != null) {
                        val favs = snapshot.documents.mapNotNull { it.id }
                        _favoritos.value = favs.toSet() // Actualizamos los equipos favoritos
                    }
                }
        }
    }

    // Función para agregar o eliminar un favorito
    fun toggleFavorito(liga: String) {
        val user = auth.currentUser ?: return
        val favRef = db.collection("users").document(user.uid).collection("favorites").document(liga)

        if (_favoritos.value.contains(liga)) {
            favRef.delete() // Eliminar el favorito
        } else {
            favRef.set(mapOf("favorito" to true)) // Agregar el favorito
        }
    }
}

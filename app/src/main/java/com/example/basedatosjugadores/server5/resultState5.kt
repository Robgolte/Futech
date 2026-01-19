package com.example.basedatosjugadores.server5

sealed class ResultState5<out T> {
    object Loading : ResultState5<Nothing>()
    data class Success<out T>(val data: T) : ResultState5<T>()
    data class Error(val error: String) : ResultState5<Nothing>()
}
package com.example.basedatosjugadores.server3

sealed class ResultState3<out T> {
    object Loading : ResultState3<Nothing>()
    data class Success<out T>(val data: T) : ResultState3<T>()
    data class Error(val error: String) : ResultState3<Nothing>()
}
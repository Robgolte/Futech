package com.example.basedatosjugadores.server4

sealed class ResultState4<out T> {
    object Loading : ResultState4<Nothing>()
    data class Success<out T>(val data: T) : ResultState4<T>()
    data class Error(val error: String) : ResultState4<Nothing>()
}
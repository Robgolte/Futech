package com.example.basedatosjugadores.server7

sealed class ResultState7<out T> {
    object Loading : ResultState7<Nothing>()
    data class Success<out T>(val data: T) : ResultState7<T>()
    data class Error(val error: String) : ResultState7<Nothing>()
}
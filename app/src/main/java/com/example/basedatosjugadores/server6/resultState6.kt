package com.example.basedatosjugadores.server6

sealed class ResultState6<out T> {
    object Loading : ResultState6<Nothing>()
    data class Success<out T>(val data: T) : ResultState6<T>()
    data class Error(val error: String) : ResultState6<Nothing>()
}
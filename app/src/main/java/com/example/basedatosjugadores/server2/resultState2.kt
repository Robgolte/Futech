package com.example.basedatosjugadores.server2

sealed class ResultState2<out T> {
    object Loading : ResultState2<Nothing>()
    data class Success<out T>(val data: T) : ResultState2<T>()
    data class Error(val error: String) : ResultState2<Nothing>()
}
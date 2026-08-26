package dev.tiagoaguiar.kingburguer.data

sealed class ApiResult<out T> {

    data class Sucess<T>(val data: T): ApiResult<T>()
    data class Error(val message: String) : ApiResult<Nothing>()
}
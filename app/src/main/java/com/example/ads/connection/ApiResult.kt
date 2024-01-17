package com.example.ads.connection

sealed class ApiResult<T>(
    val data: T? = null,
    val message: String? = null,
    val status:Int? = 0
) {
    class Success<T>(data: T? = null) : ApiResult<T>(data)
    class Error<T>(message: String?, status: Int?, data: T? = null) : ApiResult<T>(data, message, status)
}
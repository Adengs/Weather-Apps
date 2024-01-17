package com.example.ads.connection

sealed class RequestState<out R> private constructor(){
    data class Success<out T: Any>(val data : T? = null, val message: String? = null): RequestState<T>()
    data class Error(val message : String): RequestState<Nothing>()
    object Loading: RequestState<Nothing>()
}

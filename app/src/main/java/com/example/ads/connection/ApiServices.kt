package com.example.ads.connection

import com.example.ads.model.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiServices {
    @GET("weather")
    suspend fun getWeather(
        @QueryMap param: Map<String,String>?,
    ): Response
}
package com.codelabs.kepuldriver.api

import com.example.ads.model.Response
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PartMap
import retrofit2.http.QueryMap

interface ApiServices {
    @GET("weather")
    fun getWeather(
        @QueryMap param: Map<String,String>?,
    ): Call<Response>
}
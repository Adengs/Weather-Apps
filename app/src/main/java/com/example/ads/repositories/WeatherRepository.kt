package com.example.ads.repositories

import com.example.ads.connection.ApiConfig

class WeatherRepository {
    private val client = ApiConfig.getApiService()
    suspend fun getWeather(param: Map<String,String>) = client.getWeather(param)
}
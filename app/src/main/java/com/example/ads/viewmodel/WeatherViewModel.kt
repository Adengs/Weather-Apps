package com.example.ads.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.ads.connection.RequestState
import com.example.ads.model.Response
import com.example.ads.repositories.WeatherRepository
import retrofit2.HttpException

class WeatherViewModel : ViewModel() {
    private val repo : WeatherRepository = WeatherRepository()

    fun getWeather(param: Map<String, String>) : LiveData<RequestState<Response>> = liveData {
        emit(RequestState.Loading)
        try {
            val response = repo.getWeather(param)
            emit(RequestState.Success(response))
        } catch (e : HttpException){
            emit(RequestState.Error(e.response()?.errorBody().toString()))
        }
    }
}
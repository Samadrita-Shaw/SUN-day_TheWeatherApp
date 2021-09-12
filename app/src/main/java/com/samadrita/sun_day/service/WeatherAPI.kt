package com.samadrita.sun_day.service

import com.samadrita.sun_day.model.WeatherModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

//https://api.openweathermap.org/data/2.5/weather?q=jamshedpur&appid=0cce8507e62707a48e201a7436eef300

interface WeatherAPI {

    @GET("data/2.5/weather?&units=metric&appid=0cce8507e62707a48e201a7436eef300")
    fun getData(
        @Query("q") cityName : String
    ): Single<WeatherModel>
}
package com.samadrita.sun_day.service

import com.samadrita.sun_day.BuildConfig
import com.samadrita.sun_day.model.WeatherModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query



interface WeatherAPI {

    @GET("data/2.5/weather?&units=metric&appid="+ BuildConfig.API_KEY)
    fun getData(
        @Query("q") cityName : String
    ): Single<WeatherModel>
}
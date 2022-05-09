package uk.co.freemimp.weatherapp.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherService {
    @GET("data/2.5/forecast?q={city name}&appid={API key}")
    suspend fun getWeatherData(@Query("q") city: String, @Query("appid") apiKey: String): Response<Any>

    @GET("img/wn/{icon}@2x.png")
    suspend fun getWeatherIcon(@Path("icon") iconId: String): okhttp3.Response
}
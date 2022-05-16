package uk.co.freemimp.weatherapp.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import uk.co.freemimp.weatherapp.data.model.WeatherForecastResponse

interface WeatherService {
    @GET("data/2.5/forecast")
    suspend fun getWeatherForecastForCity(
        @Query("q") city: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String
    ): Response<WeatherForecastResponse>

    @GET("data/2.5/forecast")
    suspend fun getWeatherForecastForLocation(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String,
        @Query("appid") apiKey: String
    ): Response<WeatherForecastResponse>
}

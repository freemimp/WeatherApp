package uk.co.freemimp.weatherapp.domain

import uk.co.freemimp.weatherapp.domain.model.Forecast

interface WeatherApi {
    suspend fun getWeatherDataForCity(city: String): Forecast
    suspend fun getWeatherDataForLocation(latitude: Double, longitude: Double): Forecast
}

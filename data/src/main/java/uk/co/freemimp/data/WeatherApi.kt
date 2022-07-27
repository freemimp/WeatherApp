package uk.co.freemimp.data

import uk.co.freemimp.core.model.Forecast

interface WeatherApi {
    suspend fun getWeatherDataForCity(city: String): Forecast
    suspend fun getWeatherDataForLocation(latitude: Double, longitude: Double): Forecast
}

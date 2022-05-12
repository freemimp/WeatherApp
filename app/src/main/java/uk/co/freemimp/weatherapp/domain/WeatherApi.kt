package uk.co.freemimp.weatherapp.domain

import uk.co.freemimp.weatherapp.domain.model.Forecast

interface WeatherApi {
    suspend fun getWeatherData(city: String): Forecast
}

package uk.co.freemimp.weatherapp.domain.repository

import uk.co.freemimp.weatherapp.domain.model.Forecast

interface ForecastRepository {
    suspend fun get5Day3HourForecastForCity(city: String): Forecast
    suspend fun get5Day3HourForecastForCurrentLocation(
        latitude: Double,
        longitude: Double
    ): Forecast
}

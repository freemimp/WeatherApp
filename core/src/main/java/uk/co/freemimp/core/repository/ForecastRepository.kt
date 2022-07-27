package uk.co.freemimp.core.repository

import uk.co.freemimp.core.model.Forecast

interface ForecastRepository {
    suspend fun get5Day3HourForecastForCity(city: String): Forecast
    suspend fun get5Day3HourForecastForCurrentLocation(
        latitude: Double,
        longitude: Double
    ): Forecast
}

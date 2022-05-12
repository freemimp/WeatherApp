package uk.co.freemimp.weatherapp.domain.repository

import uk.co.freemimp.weatherapp.domain.model.Forecast

interface ForecastRepository {
    suspend fun get5Day3HourForecast(city: String): Forecast
}

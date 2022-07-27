package uk.co.freemimp.data

import javax.inject.Inject
import uk.co.freemimp.core.model.Forecast
import uk.co.freemimp.core.repository.ForecastRepository

class ForecastRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi
) : ForecastRepository {

    override suspend fun get5Day3HourForecastForCity(city: String): Forecast {
        return weatherApi.getWeatherDataForCity(city)
    }

    override suspend fun get5Day3HourForecastForCurrentLocation(
        latitude: Double,
        longitude: Double
    ): Forecast {
        return weatherApi.getWeatherDataForLocation(longitude, latitude)
    }
}

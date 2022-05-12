package uk.co.freemimp.weatherapp.domain.repository

import uk.co.freemimp.weatherapp.domain.WeatherApi
import uk.co.freemimp.weatherapp.domain.model.Forecast
import javax.inject.Inject

class ForecastRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi
    ) : ForecastRepository {

    override suspend fun get5Day3HourForecast(city: String): Forecast {
       return weatherApi.getWeatherData(city)
    }
}

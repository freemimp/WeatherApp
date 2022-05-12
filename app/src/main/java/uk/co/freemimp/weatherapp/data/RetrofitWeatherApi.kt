package uk.co.freemimp.weatherapp.data

import retrofit2.HttpException
import uk.co.freemimp.weatherapp.BuildConfig
import uk.co.freemimp.weatherapp.domain.mapper.ForecastMapper
import uk.co.freemimp.weatherapp.domain.WeatherApi
import uk.co.freemimp.weatherapp.domain.model.Forecast
import javax.inject.Inject

class RetrofitWeatherApi @Inject constructor(
    private val weatherService: WeatherService,
    private val forecastMapper: ForecastMapper
) : WeatherApi {
    override suspend fun getWeatherData(city: String): Forecast {
        return try {
            val response = weatherService.getWeatherForecastForCity(
                city = city,
                units = UNITS,
                apiKey = BuildConfig.API_KEY
            )
            if (response.isSuccessful) {
                val body =
                    requireNotNull(response.body()) { "If response is successful, then body must not be null" }
                forecastMapper.map(body)
            } else {
                throw HttpException(response)
            }
        } catch (t: Throwable) {
            throw t
        }
    }
}

private const val UNITS = "metric"

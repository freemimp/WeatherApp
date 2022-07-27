package uk.co.freemimp.data

import retrofit2.HttpException

import javax.inject.Inject
import uk.co.freemimp.core.model.Forecast
import uk.co.freemimp.data.mapper.ForecastMapper

class RetrofitWeatherApi @Inject constructor(
    private val weatherService: WeatherService,
    private val forecastMapper: ForecastMapper
) : WeatherApi {
    override suspend fun getWeatherDataForCity(city: String): Forecast {
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

    override suspend fun getWeatherDataForLocation(
        latitude: Double,
        longitude: Double
    ): Forecast {
        return try {
            val response = weatherService.getWeatherForecastForLocation(
                latitude = latitude,
                longitude = longitude,
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

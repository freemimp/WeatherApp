package uk.co.freemimp.weatherapp.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uk.co.freemimp.weatherapp.domain.model.Forecast
import uk.co.freemimp.weatherapp.domain.repository.ForecastRepository
import javax.inject.Inject

class Get5Day3HourForecastUseCase @Inject constructor(private val repository: ForecastRepository) {

    suspend fun execute(city: String): Forecast {
        return withContext(Dispatchers.IO) {
            repository.get5Day3HourForecast(city)
        }
    }
}
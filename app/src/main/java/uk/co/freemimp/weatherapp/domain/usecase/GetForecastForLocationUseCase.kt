package uk.co.freemimp.weatherapp.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uk.co.freemimp.weatherapp.domain.model.Forecast
import uk.co.freemimp.weatherapp.domain.repository.ForecastRepository
import javax.inject.Inject

class GetForecastForLocationUseCase @Inject constructor(private val repository: ForecastRepository) {
    suspend fun execute(latitude: Double, longitude: Double): Forecast =
        withContext(Dispatchers.IO) {
            repository.get5Day3HourForecastForCurrentLocation(
                latitude = latitude,
                longitude = longitude
            )
        }
}

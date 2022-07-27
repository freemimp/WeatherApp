package uk.co.freemimp.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uk.co.freemimp.core.model.Forecast
import uk.co.freemimp.core.repository.ForecastRepository
import javax.inject.Inject

class GetForecastForLocationUseCase @Inject constructor(private val repository: uk.co.freemimp.core.repository.ForecastRepository) {
    suspend fun execute(latitude: Double, longitude: Double): uk.co.freemimp.core.model.Forecast =
        withContext(Dispatchers.IO) {
            repository.get5Day3HourForecastForCurrentLocation(
                latitude = latitude,
                longitude = longitude
            )
        }
}

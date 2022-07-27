package uk.co.freemimp.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uk.co.freemimp.core.model.Forecast
import uk.co.freemimp.core.repository.ForecastRepository
import javax.inject.Inject

class GetForecastForCityUseCase @Inject constructor(private val repository: ForecastRepository) {

    suspend fun execute(city: String): Forecast =
        withContext(Dispatchers.IO) {
            repository.get5Day3HourForecastForCity(city)
        }
}

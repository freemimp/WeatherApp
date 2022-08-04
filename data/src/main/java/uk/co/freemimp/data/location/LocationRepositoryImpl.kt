package uk.co.freemimp.data.location

import kotlinx.coroutines.ExperimentalCoroutinesApi
import uk.co.freemimp.core.location.LocationRepository
import javax.inject.Inject

@ExperimentalCoroutinesApi
class LocationRepositoryImpl @Inject constructor(
        private val sharedLocationManager: SharedLocationManager
) : LocationRepository {

    override fun getLocation() = sharedLocationManager.locationFlow()
}

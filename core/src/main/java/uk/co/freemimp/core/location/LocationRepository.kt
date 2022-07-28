package uk.co.freemimp.core.location

import android.location.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface LocationRepository {
    fun getLocations(): Flow<Location>
}

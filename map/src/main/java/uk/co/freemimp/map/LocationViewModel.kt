package uk.co.freemimp.map

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

import uk.co.freemimp.core.location.LocationRepository
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    locationRepository: LocationRepository
) : ViewModel() {

    val location = locationRepository.getLocations()
}

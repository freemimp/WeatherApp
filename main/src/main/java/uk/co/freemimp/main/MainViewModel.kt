package uk.co.freemimp.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.co.freemimp.core.location.LocationRepository
import uk.co.freemimp.core.model.DayWeather
import uk.co.freemimp.domain.usecase.GetForecastForCityUseCase
import uk.co.freemimp.domain.usecase.GetForecastForLocationUseCase
import uk.co.freemimp.domain.usecase.GetLocationFormattedUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    locationRepository: LocationRepository,
    private val getForecastForCityUseCase: GetForecastForCityUseCase,
    private val getForecastForLocationUseCase: GetForecastForLocationUseCase,
    private val getLocationFormattedUseCase: GetLocationFormattedUseCase
) : ViewModel() {

    val getLocation = locationRepository.getLocations()

    private val _weatherLocationName = MutableStateFlow<String>("")
    val weatherLocationName: StateFlow<String> = _weatherLocationName

    private val _day1Forecast = MutableStateFlow<List<DayWeather>>(emptyList())
    val day1Forecast: StateFlow<List<DayWeather>> = _day1Forecast
    private val _day2Forecast = MutableStateFlow<List<DayWeather>>(emptyList())
    val day2Forecast: StateFlow<List<DayWeather>> = _day2Forecast
    private val _day3Forecast = MutableStateFlow<List<DayWeather>>(emptyList())
    val day3Forecast: StateFlow<List<DayWeather>> = _day3Forecast
    private val _day4Forecast = MutableStateFlow<List<DayWeather>>(emptyList())
    val day4Forecast: StateFlow<List<DayWeather>> = _day4Forecast
    private val _day5Forecast = MutableStateFlow<List<DayWeather>>(emptyList())
    val day5Forecast: StateFlow<List<DayWeather>> = _day5Forecast

    private val _navigateToMap = MutableSharedFlow<Pair<Float, Float>>(replay = 0)
    val navigateToMap: SharedFlow<Pair<Float, Float>> = _navigateToMap

    private val _showLoading = MutableSharedFlow<Boolean>(replay = 0)
    val showLoading: SharedFlow<Boolean> = _showLoading

    private val _showError = MutableSharedFlow<Boolean>(replay = 0)
    val showError: SharedFlow<Boolean> = _showError

    private val _showLocationError = MutableSharedFlow<Boolean>(replay = 0)
    val showLocationError: SharedFlow<Boolean> = _showLocationError

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        viewModelScope.launch {
            _showError.emit(true)
            _showLoading.emit(false)
        }
    }

    fun showForecastForTheCity(city: String) {
        viewModelScope.launch(exceptionHandler) {
            _showError.emit(false)
            _showLoading.emit(true)
            val forecast = getForecastForCityUseCase.execute(city)
            _weatherLocationName.value = city
            _day1Forecast.value = forecast.day1
            _day2Forecast.value = forecast.day2
            _day3Forecast.value = forecast.day3
            _day4Forecast.value = forecast.day4
            _day5Forecast.value = forecast.day5

            _showLoading.emit(false)
        }
    }

    fun showForecastForCurrentLocation(latitude: Double?, longitude: Double?) {
        viewModelScope.launch(exceptionHandler) {
            if (latitude == null || longitude == null) {
                _showLocationError.emit(true)
            } else {
                _showError.emit(false)
                _showLocationError.emit(false)
                _showLoading.emit(true)
                val forecast = getForecastForLocationUseCase.execute(latitude, longitude)
                _weatherLocationName.value =
                    getLocationFormattedUseCase.execute(
                        latitude,
                        longitude
                    )
                _day1Forecast.value = forecast.day1
                _day2Forecast.value = forecast.day2
                _day3Forecast.value = forecast.day3
                _day4Forecast.value = forecast.day4
                _day5Forecast.value = forecast.day5

                _showLoading.emit(false)
            }
        }
    }

    fun navigateToMapFragment(latitude: Double?, longitude: Double?) {
        viewModelScope.launch {
            if (latitude == null || longitude == null) {
                _showLocationError.emit(true)
            } else {
                _navigateToMap.emit(Pair(latitude.toFloat(), longitude.toFloat()))
                _showLocationError.emit(false)
            }
        }
    }
}

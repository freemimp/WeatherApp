package uk.co.freemimp.weatherapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import uk.co.freemimp.weatherapp.domain.model.DayWeather
import uk.co.freemimp.weatherapp.domain.usecase.Get5Day3HourForecastUseCase
import uk.co.freemimp.weatherapp.mvvm.Event
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val get5Day3HourForecastUseCase: Get5Day3HourForecastUseCase
) : ViewModel() {

    private val _day1Forecast = MutableLiveData<List<DayWeather>>()
    val day1Forecast: LiveData<List<DayWeather>> = _day1Forecast
    private val _day2Forecast = MutableLiveData<List<DayWeather>>()
    val day2Forecast: LiveData<List<DayWeather>> = _day2Forecast
    private val _day3Forecast = MutableLiveData<List<DayWeather>>()
    val day3Forecast: LiveData<List<DayWeather>> = _day3Forecast
    private val _day4Forecast = MutableLiveData<List<DayWeather>>()
    val day4Forecast: LiveData<List<DayWeather>> = _day4Forecast
    private val _day5Forecast = MutableLiveData<List<DayWeather>>()
    val day5Forecast: LiveData<List<DayWeather>> = _day5Forecast

    private val _showLoading = MutableLiveData<Event<Boolean>>()
    val showLoading: LiveData<Event<Boolean>> = _showLoading

    private val _showError = MutableLiveData<Event<Boolean>>()
    val showError: LiveData<Event<Boolean>> = _showError

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        _showError.postValue(Event(true))
        _showLoading.postValue(Event(false))
    }

    fun showForecastForTheCity(city: String) {
        viewModelScope.launch(exceptionHandler) {
            _showLoading.postValue(Event(true))
            val forecast = get5Day3HourForecastUseCase.execute(city)
            _day1Forecast.postValue(forecast.day1)
            _day2Forecast.postValue(forecast.day2)
            _day3Forecast.postValue(forecast.day3)
            _day4Forecast.postValue(forecast.day4)
            _day5Forecast.postValue(forecast.day5)

            _showLoading.postValue(Event(false))
        }
    }
}

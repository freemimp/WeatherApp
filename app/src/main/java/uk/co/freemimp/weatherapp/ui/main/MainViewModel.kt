package uk.co.freemimp.weatherapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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


    private val _showLoading = MutableLiveData<Event<Boolean>>()
    val showLoading: LiveData<Event<Boolean>> = _showLoading

    private val _showError = MutableLiveData<Event<Boolean>>()
    val showError: LiveData<Event<Boolean>> = _showError

    fun showForecastForTheCity(city: String) {
        viewModelScope.launch {
            _showLoading.postValue(Event(true))
            val forecast = get5Day3HourForecastUseCase.execute(city)
            _day1Forecast.postValue(forecast.day1)

            _showLoading.postValue(Event(false))
        }
    }
}

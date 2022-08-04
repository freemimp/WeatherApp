package uk.co.freemimp.data.mapper

import java.time.LocalDate
import javax.inject.Inject
import uk.co.freemimp.core.model.DayWeather
import uk.co.freemimp.core.model.Forecast
import uk.co.freemimp.data.model.WeatherForecastResponse

class ForecastMapper @Inject constructor() {

    fun map(response: WeatherForecastResponse): Forecast {
        val fiveDayWeather = mapDayWeather(response.weather)
        val fiveDayWeatherByDay =
            fiveDayWeather.groupBy { LocalDate.parse(it.date).dayOfMonth }.toMap()
        val keys = fiveDayWeatherByDay.keys.toList()
        val day1Weather =
            requireNotNull(fiveDayWeatherByDay[keys[0]]) { "Weather is not expected to be null at this point" }
        val day2Weather =
            requireNotNull(fiveDayWeatherByDay[keys[1]]) { "Weather is not expected to be null at this point" }
        val day3Weather =
            requireNotNull(fiveDayWeatherByDay[keys[2]]) { "Weather is not expected to be null at this point" }
        val day4Weather =
            requireNotNull(fiveDayWeatherByDay[keys[3]]) { "Weather is not expected to be null at this point" }
        val day5Weather =
            requireNotNull(fiveDayWeatherByDay[keys[4]]) { "Weather is not expected to be null at this point" }

        return Forecast(day1Weather, day2Weather, day3Weather, day4Weather, day5Weather)
    }

    private fun mapDayWeather(weather: List<WeatherForecastResponse.Weather>): List<DayWeather> {
        return weather.map {
            DayWeather(
                it.dtTxt.substringBefore(" "),
                it.dtTxt.substringAfter(" "),
                it.main.temp,
                createIconUrl(it.weatherIcons.first().icon)
            )
        }
    }

    private fun createIconUrl(iconId: String): String {
        return "https://openweathermap.org/img/wn/$iconId@2x.png"
    }
}

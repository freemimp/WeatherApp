package uk.co.freemimp.core.model

data class Forecast(
    val day1: List<DayWeather>,
    val day2: List<DayWeather>,
    val day3: List<DayWeather>,
    val day4: List<DayWeather>,
    val day5: List<DayWeather>,
)

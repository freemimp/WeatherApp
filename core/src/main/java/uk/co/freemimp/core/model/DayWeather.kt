package uk.co.freemimp.core.model

data class DayWeather(
    val date: String,
    val time: String,
    val temperature: Double,
    val iconUrl: String
)

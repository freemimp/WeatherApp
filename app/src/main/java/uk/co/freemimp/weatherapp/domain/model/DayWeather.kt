package uk.co.freemimp.weatherapp.domain.model

import java.time.Instant

data class DayWeather(
    val date: String,
    val time: String,
    val temperature: Double,
    val iconUrl: String
)

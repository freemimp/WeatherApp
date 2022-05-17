package uk.co.freemimp.weatherapp.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherForecastResponse(
    @Json(name = "cod")
    val cod: String,
    @Json(name = "message")
    val message: Int,
    @Json(name = "cnt")
    val cnt: Int,
    @Json(name = "list")
    val weather: List<Weather>,
    @Json(name = "city")
    val city: City
) {
    @JsonClass(generateAdapter = true)
    data class Weather(
        @Json(name = "dt")
        val dt: Int,
        @Json(name = "main")
        val main: Main,
        @Json(name = "weather")
        val weatherIcons: List<WeatherIcon>,
        @Json(name = "clouds")
        val clouds: Clouds,
        @Json(name = "wind")
        val wind: Wind,
        @Json(name = "visibility")
        val visibility: Int,
        @Json(name = "pop")
        val pop: Double,
        @Json(name = "sys")
        val sys: Sys,
        @Json(name = "dt_txt")
        val dtTxt: String,
        @Json(name = "rain")
        val rain: Rain?
    ) {
        @JsonClass(generateAdapter = true)
        data class Main(
            @Json(name = "temp")
            val temp: Double,
            @Json(name = "feels_like")
            val feelsLike: Double,
            @Json(name = "temp_min")
            val tempMin: Double,
            @Json(name = "temp_max")
            val tempMax: Double,
            @Json(name = "pressure")
            val pressure: Int,
            @Json(name = "sea_level")
            val seaLevel: Int,
            @Json(name = "grnd_level")
            val grndLevel: Int,
            @Json(name = "humidity")
            val humidity: Int,
            @Json(name = "temp_kf")
            val tempKf: Double
        )

        @JsonClass(generateAdapter = true)
        data class WeatherIcon(
            @Json(name = "id")
            val id: Int,
            @Json(name = "main")
            val main: String,
            @Json(name = "description")
            val description: String,
            @Json(name = "icon")
            val icon: String
        )

        @JsonClass(generateAdapter = true)
        data class Clouds(
            @Json(name = "all")
            val all: Int
        )

        @JsonClass(generateAdapter = true)
        data class Wind(
            @Json(name = "speed")
            val speed: Double,
            @Json(name = "deg")
            val deg: Int,
            @Json(name = "gust")
            val gust: Double
        )

        @JsonClass(generateAdapter = true)
        data class Sys(
            @Json(name = "pod")
            val pod: String
        )

        @JsonClass(generateAdapter = true)
        data class Rain(
            @Json(name = "3h")
            val h: Double
        )
    }

    @JsonClass(generateAdapter = true)
    data class City(
        @Json(name = "id")
        val id: Int,
        @Json(name = "name")
        val name: String,
        @Json(name = "coord")
        val coord: Coord,
        @Json(name = "country")
        val country: String,
        @Json(name = "population")
        val population: Int,
        @Json(name = "timezone")
        val timezone: Int,
        @Json(name = "sunrise")
        val sunrise: Int,
        @Json(name = "sunset")
        val sunset: Int
    ) {
        @JsonClass(generateAdapter = true)
        data class Coord(
            @Json(name = "lat")
            val lat: Double,
            @Json(name = "lon")
            val lon: Double
        )
    }
}

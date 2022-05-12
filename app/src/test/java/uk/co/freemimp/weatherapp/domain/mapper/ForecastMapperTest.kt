package uk.co.freemimp.weatherapp.domain.mapper

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import uk.co.freemimp.weatherapp.data.model.WeatherForecastResponse
import uk.co.freemimp.weatherapp.domain.model.DayWeather
import uk.co.freemimp.weatherapp.domain.model.Forecast

@ExtendWith(MockKExtension::class)
internal class ForecastMapperTest {

    @InjectMockKs
    private lateinit var sut: ForecastMapper

    @RelaxedMockK
    private lateinit var response: WeatherForecastResponse

    @RelaxedMockK
    private lateinit var weather1: WeatherForecastResponse.Weather
    @RelaxedMockK
    private lateinit var weather2: WeatherForecastResponse.Weather
    @RelaxedMockK
    private lateinit var weather3: WeatherForecastResponse.Weather
    @RelaxedMockK
    private lateinit var weather4: WeatherForecastResponse.Weather
    @RelaxedMockK
    private lateinit var weather5: WeatherForecastResponse.Weather
    @RelaxedMockK
    private lateinit var weatherIcon: WeatherForecastResponse.Weather.WeatherIcon

    @BeforeEach
    internal fun setUp() {
        every { weatherIcon.icon } returns ICON

        every { weather1.dtTxt } returns DAY1
        every { weather1.weatherIcons } returns listOf(weatherIcon)
        every { weather2.dtTxt } returns DAY2
        every { weather2.weatherIcons } returns listOf(weatherIcon)

        every { weather3.dtTxt } returns DAY3
        every { weather3.weatherIcons } returns listOf(weatherIcon)

        every { weather4.dtTxt } returns DAY4
        every { weather4.weatherIcons } returns listOf(weatherIcon)

        every { weather5.dtTxt } returns DAY5
        every { weather5.weatherIcons } returns listOf(weatherIcon)

        every { response.weather } returns listOf(weather1, weather2, weather3, weather4, weather5)
    }

    @Test
    fun `given map is invoked, then map weather response to forecast`() {
        val result = sut.map(response)
        // I only mocked what I need in MockK, with relaxed mockK double is replaced with 0.0 (default value)
        val expected = Forecast(
            day1 = listOf(DayWeather("2022-05-11", "00:00:00", 0.0, "https://openweathermap.org/img/wn/$ICON@2x.png")),
            day2 = listOf(DayWeather("2022-05-12", "00:00:00", 0.0, "https://openweathermap.org/img/wn/$ICON@2x.png")),
            day3 = listOf(DayWeather("2022-05-13", "00:00:00", 0.0, "https://openweathermap.org/img/wn/$ICON@2x.png")),
            day4 = listOf(DayWeather("2022-05-14", "00:00:00", 0.0, "https://openweathermap.org/img/wn/$ICON@2x.png")),
            day5 = listOf(DayWeather("2022-05-15", "00:00:00", 0.0, "https://openweathermap.org/img/wn/$ICON@2x.png")),
            )

        assertEquals(expected, result)
    }
}

private const val DAY1 = "2022-05-11 00:00:00"
private const val DAY2 = "2022-05-12 00:00:00"
private const val DAY3 = "2022-05-13 00:00:00"
private const val DAY4 = "2022-05-14 00:00:00"
private const val DAY5 = "2022-05-15 00:00:00"
private const val ICON = "4dp"

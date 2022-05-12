package uk.co.freemimp.weatherapp.domain.repository

import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions.*
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import uk.co.freemimp.weatherapp.domain.WeatherApi
import uk.co.freemimp.weatherapp.domain.model.Forecast
import uk.co.freemimp.weatherapp.utils.TestException

@ExtendWith(MockKExtension::class)
internal class ForecastRepositoryImplTest {

    @MockK
    private lateinit var weatherApi: WeatherApi
    @InjectMockKs
    private lateinit var sut: ForecastRepositoryImpl

    @Nested
    @DisplayName("given get5Day3HourForecast is invoked, ")
    inner class Get5Day3HourForecast {

        @Test
        fun `when api call is successful, then returns forecast`() {
            runBlocking {
                val forecast = mockk<Forecast>()
                coEvery { weatherApi.getWeatherData(any()) } returns forecast

                val result = sut.get5Day3HourForecast(city = CITY)

                assertEquals(forecast, result)
            }
        }

        @Test
        fun `when api call is NOT successful, then throw exception`() {
            runBlocking {
                coEvery { weatherApi.getWeatherData(any()) } throws TestException

                assertThrows<TestException> { sut.get5Day3HourForecast(city = CITY) }
            }
        }
    }
}

private const val CITY = "London"

package uk.co.freemimp.weatherapp.domain.repository

import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
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
    @DisplayName("given get5Day3HourForecastForCity is invoked, ")
    inner class Get5Day3HourForecast {

        private val city = "London"

        @Test
        fun `when api call is successful, then returns forecast`() {
            runBlocking {
                val forecast = mockk<Forecast>()
                coEvery { weatherApi.getWeatherDataForCity(any()) } returns forecast

                val result = sut.get5Day3HourForecastForCity(city = city)

                assertEquals(forecast, result)
            }
        }

        @Test
        fun `when api call is NOT successful, then throw exception`() {
            runBlocking {
                coEvery { weatherApi.getWeatherDataForCity(any()) } throws TestException

                assertThrows<TestException> { sut.get5Day3HourForecastForCity(city = city) }
            }
        }
    }

    @Nested
    @DisplayName("given get5Day3HourForecastForCurrentLocation is invoked, ")
    inner class Get5Day3HourForecastForCurrentLocation {

        private val latitude = 0.0
        private val longitude = 1.0

        @Test
        fun `when api call is successful, then returns forecast`() {
            runBlocking {
                val forecast = mockk<Forecast>()
                coEvery { weatherApi.getWeatherDataForLocation(any(), any()) } returns forecast

                val result = sut.get5Day3HourForecastForCurrentLocation(
                    latitude = latitude,
                    longitude = longitude
                )

                assertEquals(forecast, result)
            }
        }

        @Test
        fun `when api call is NOT successful, then throw exception`() {
            runBlocking {
                coEvery { weatherApi.getWeatherDataForLocation(any(), any()) } throws TestException

                assertThrows<TestException> {
                    sut.get5Day3HourForecastForCurrentLocation(
                        latitude = latitude,
                        longitude = longitude
                    )
                }
            }
        }
    }
}

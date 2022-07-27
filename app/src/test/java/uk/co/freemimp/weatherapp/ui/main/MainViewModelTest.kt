package uk.co.freemimp.weatherapp.ui.main

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import uk.co.freemimp.commontest.utils.TestCoroutineExtension
import uk.co.freemimp.commontest.utils.TestException
import uk.co.freemimp.core.model.DayWeather
import uk.co.freemimp.core.model.Forecast

@ExperimentalCoroutinesApi
@ExperimentalTime
@ExtendWith(MockKExtension::class, TestCoroutineExtension::class)
internal class MainViewModelTest {

    @MockK
    private lateinit var getForecastForCityUseCase: uk.co.freemimp.domain.usecase.GetForecastForCityUseCase

    @MockK
    private lateinit var getForecastForLocationUseCase: uk.co.freemimp.domain.usecase.GetForecastForLocationUseCase

    @MockK
    private lateinit var getLocationFormattedUseCase: uk.co.freemimp.domain.usecase.GetLocationFormattedUseCase

    @InjectMockKs
    private lateinit var sut: MainViewModel

    @Nested
    @DisplayName("given showForecastForTheCity was invoked, ")
    inner class ShowForecastForTheCity {

        private val city = "London"

        @Test
        fun `when use case is successful, then notify correct observers`() {
            runTest {
                val dayWeather1 = mockk<DayWeather>()
                val dayWeather2 = mockk<DayWeather>()
                val dayWeather3 = mockk<DayWeather>()
                val dayWeather4 = mockk<DayWeather>()
                val dayWeather5 = mockk<DayWeather>()
                val forecast = mockk<Forecast> {
                    every { day1 } returns listOf(dayWeather1)
                    every { day2 } returns listOf(dayWeather2)
                    every { day3 } returns listOf(dayWeather3)
                    every { day4 } returns listOf(dayWeather4)
                    every { day5 } returns listOf(dayWeather5)
                }
                coEvery { getForecastForCityUseCase.execute(city = city) } returns forecast

                sut.showForecastForTheCity(city)

                sut.showError.test {
                    sut.showForecastForTheCity(city)

                    assertFalse(
                        awaitItem()
                    )

                }
                sut.showLoading.test {
                    sut.showForecastForTheCity(city)

                    assertTrue(awaitItem())
                    assertFalse(awaitItem())
                }
                sut.weatherLocationName.test { assertEquals(city, awaitItem()) }
                sut.day1Forecast.test { assertEquals(listOf(dayWeather1), awaitItem()) }
                sut.day2Forecast.test { assertEquals(listOf(dayWeather2), awaitItem()) }
                sut.day3Forecast.test { assertEquals(listOf(dayWeather3), awaitItem()) }
                sut.day4Forecast.test { assertEquals(listOf(dayWeather4), awaitItem()) }
                sut.day5Forecast.test { assertEquals(listOf(dayWeather5), awaitItem()) }

                coVerify {
                    getForecastForCityUseCase.execute(city)
                }
            }
        }

        @Test
        fun `when use case is NOT successful, then notify correct observers`() {
            runTest {
                coEvery { getForecastForCityUseCase.execute(city = city) } throws TestException

                sut.showError.test {
                    sut.showForecastForTheCity(city)

                    assertFalse(
                        awaitItem()
                    )
                    assertTrue(
                        awaitItem()
                    )
                }
                sut.showLoading.test {
                    sut.showForecastForTheCity(city)

                    assertTrue(awaitItem())
                    assertFalse(awaitItem())
                }


                coVerify {
                    getForecastForCityUseCase.execute(city = city)
                }

            }
        }
    }

    @Nested
    @DisplayName("given showForecastForCurrentLocation was invoked, ")
    inner class ShowForecastForCurrentLocation {

        private val latitude = 1.2345
        private val longitude = 6.7890

        @Test
        fun `when use case is successful, then notify correct observers`() {
            runTest {
                val formattedLocation = "$latitude ° N, $longitude ° E"
                val dayWeather1 = mockk<DayWeather>()
                val dayWeather2 = mockk<DayWeather>()
                val dayWeather3 = mockk<DayWeather>()
                val dayWeather4 = mockk<DayWeather>()
                val dayWeather5 = mockk<DayWeather>()
                val forecast = mockk<Forecast> {
                    every { day1 } returns listOf(dayWeather1)
                    every { day2 } returns listOf(dayWeather2)
                    every { day3 } returns listOf(dayWeather3)
                    every { day4 } returns listOf(dayWeather4)
                    every { day5 } returns listOf(dayWeather5)
                }
                every {
                    getLocationFormattedUseCase.execute(
                        latitude = latitude,
                        longitude = longitude
                    )
                } returns formattedLocation
                coEvery {
                    getForecastForLocationUseCase.execute(
                        latitude = latitude,
                        longitude = longitude
                    )
                } returns forecast

                sut.showForecastForCurrentLocation(latitude = latitude, longitude = longitude)

                sut.showError.test {
                    sut.showForecastForCurrentLocation(latitude = latitude, longitude = longitude)

                    assertFalse(
                        awaitItem()
                    )
                }
                sut.showLoading.test {
                    sut.showForecastForCurrentLocation(latitude = latitude, longitude = longitude)

                    assertTrue(awaitItem())
                    assertFalse(awaitItem())
                }

                sut.weatherLocationName.test { assertEquals(formattedLocation, awaitItem()) }
                sut.day1Forecast.test { assertEquals(listOf(dayWeather1), awaitItem()) }
                sut.day2Forecast.test { assertEquals(listOf(dayWeather2), awaitItem()) }
                sut.day3Forecast.test { assertEquals(listOf(dayWeather3), awaitItem()) }
                sut.day4Forecast.test { assertEquals(listOf(dayWeather4), awaitItem()) }
                sut.day5Forecast.test { assertEquals(listOf(dayWeather5), awaitItem()) }

                sut.showLocationError.test {
                    sut.showForecastForCurrentLocation(latitude = latitude, longitude = longitude)

                    assertFalse(
                        awaitItem()
                    )
                }

                coVerify {
                    getForecastForLocationUseCase.execute(latitude, longitude)
                }
            }
        }

        @Test
        fun `when use case is NOT successful, then notify correct observers`() {
            runTest {
                coEvery {
                    getForecastForLocationUseCase.execute(
                        latitude = latitude,
                        longitude = longitude
                    )
                } throws TestException

                sut.showError.test {
                    sut.showForecastForCurrentLocation(latitude = latitude, longitude = longitude)

                    assertFalse(awaitItem())
                    assertTrue(awaitItem())

                }
                sut.showLoading.test {
                    sut.showForecastForCurrentLocation(latitude = latitude, longitude = longitude)

                    assertTrue(awaitItem())
                    assertFalse(awaitItem())
                }


                coVerify {
                    getForecastForLocationUseCase.execute(latitude, longitude)
                }
            }
        }

        @Test
        fun `when latitude and longitude are null, then notify correct observers`() {
            runTest {
                sut.showLocationError.test {
                    sut.showForecastForCurrentLocation(latitude = null, longitude = null)
                    assertTrue(awaitItem())
                }
            }
        }
    }

    @Nested
    @DisplayName("given navigateToMapFragment was invoked, ")
    inner class NavigateToMapFragment {

        private val latitude = 1.2345
        private val longitude = 6.7890

        @Test
        fun `when latitude and longitude is not null, then notify navigateToMap observer`() {
            runTest {
                sut.navigateToMap.test {
                    sut.navigateToMapFragment(latitude, longitude)
                    assertEquals(
                        Pair(latitude.toFloat(), longitude.toFloat()),
                        awaitItem()
                    )
                }
                sut.showLocationError.test {
                    sut.navigateToMapFragment(latitude, longitude)
                    assertFalse(
                        awaitItem()
                    )
                }
            }
        }

        @Test
        fun `when latitude and longitude are null, then notify howLocationError observer`() {
            runTest {
                sut.showLocationError.test {
                    sut.navigateToMapFragment(latitude = null, longitude = null)

                    assertTrue(
                        awaitItem()
                    )
                }
            }
        }
    }
}

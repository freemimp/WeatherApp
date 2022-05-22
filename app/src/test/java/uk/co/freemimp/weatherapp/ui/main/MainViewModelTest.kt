package uk.co.freemimp.weatherapp.ui.main

import androidx.lifecycle.Observer
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import uk.co.freemimp.weatherapp.domain.model.DayWeather
import uk.co.freemimp.weatherapp.domain.model.Forecast
import uk.co.freemimp.weatherapp.domain.usecase.GetForecastForCityUseCase
import uk.co.freemimp.weatherapp.domain.usecase.GetForecastForLocationUseCase
import uk.co.freemimp.weatherapp.domain.usecase.GetLocationFormattedUseCase
import uk.co.freemimp.weatherapp.mvvm.Event
import uk.co.freemimp.weatherapp.utils.InstantTaskExecutorExtension
import uk.co.freemimp.weatherapp.utils.TestException

@ExtendWith(MockKExtension::class, InstantTaskExecutorExtension::class)
internal class MainViewModelTest {

    @MockK
    private lateinit var getForecastForCityUseCase: GetForecastForCityUseCase

    @MockK
    private lateinit var getForecastForLocationUseCase: GetForecastForLocationUseCase

    @MockK
    private lateinit var getLocationFormattedUseCase: GetLocationFormattedUseCase

    @InjectMockKs
    private lateinit var sut: MainViewModel

    @RelaxedMockK
    private lateinit var weatherLocationNameObserver: Observer<String>

    @RelaxedMockK
    private lateinit var day1ForecastObserver: Observer<List<DayWeather>>

    @RelaxedMockK
    private lateinit var day2ForecastObserver: Observer<List<DayWeather>>

    @RelaxedMockK
    private lateinit var day3ForecastObserver: Observer<List<DayWeather>>

    @RelaxedMockK
    private lateinit var day4ForecastObserver: Observer<List<DayWeather>>

    @RelaxedMockK
    private lateinit var day5ForecastObserver: Observer<List<DayWeather>>

    @RelaxedMockK
    private lateinit var showLoadingObserver: Observer<Event<Boolean>>

    @RelaxedMockK
    private lateinit var navigateToMapObserver: Observer<Event<Pair<Float, Float>>>

    @RelaxedMockK
    private lateinit var showErrorObserver: Observer<Event<Boolean>>

    @RelaxedMockK
    private lateinit var showLocationErrorObserver: Observer<Event<Boolean>>

    @BeforeEach
    fun setUp() {
        sut.weatherLocationName.observeForever(weatherLocationNameObserver)
        sut.day1Forecast.observeForever(day1ForecastObserver)
        sut.day2Forecast.observeForever(day2ForecastObserver)
        sut.day3Forecast.observeForever(day3ForecastObserver)
        sut.day4Forecast.observeForever(day4ForecastObserver)
        sut.day5Forecast.observeForever(day5ForecastObserver)
        sut.showLoading.observeForever(showLoadingObserver)
        sut.navigateToMap.observeForever(navigateToMapObserver)
        sut.showError.observeForever(showErrorObserver)
        sut.showLocationError.observeForever(showLocationErrorObserver)
    }

    @Nested
    @DisplayName("given showForecastForTheCity was invoked, ")
    inner class ShowForecastForTheCity {

        private val city = "London"

        @Test
        fun `when use case is successful, then notify correct observers`() {

            val dayWeather1 = mockk<DayWeather>()
            val dayWeather2 = mockk<DayWeather>()
            val dayWeather3 = mockk<DayWeather>()
            val dayWeather4 = mockk<DayWeather>()
            val dayWeather5 = mockk<DayWeather>()
            val forecast = mockk<Forecast>() {
                every { day1 } returns listOf(dayWeather1)
                every { day2 } returns listOf(dayWeather2)
                every { day3 } returns listOf(dayWeather3)
                every { day4 } returns listOf(dayWeather4)
                every { day5 } returns listOf(dayWeather5)
            }
            coEvery { getForecastForCityUseCase.execute(city = city) } returns forecast

            sut.showForecastForTheCity(city)

            coVerifySequence {
                showErrorObserver.onChanged(withArg {
                    assertFalse(
                        it.peekContent()
                    )
                })
                showLoadingObserver.onChanged(withArg { assertTrue(it.peekContent()) })
                getForecastForCityUseCase.execute(city)
                weatherLocationNameObserver.onChanged(withArg { assertEquals(city, it) })
                day1ForecastObserver.onChanged(withArg { assertEquals(listOf(dayWeather1), it) })
                day2ForecastObserver.onChanged(withArg { assertEquals(listOf(dayWeather2), it) })
                day3ForecastObserver.onChanged(withArg { assertEquals(listOf(dayWeather3), it) })
                day4ForecastObserver.onChanged(withArg { assertEquals(listOf(dayWeather4), it) })
                day5ForecastObserver.onChanged(withArg { assertEquals(listOf(dayWeather5), it) })

                showLoadingObserver.onChanged(withArg { assertFalse(it.peekContent()) })
            }
        }

        @Test
        fun `when use case is NOT successful, then notify correct observers`() {
            coEvery { getForecastForCityUseCase.execute(city = city) } throws TestException

            sut.showForecastForTheCity(city)

            coVerifySequence {
                showErrorObserver.onChanged(withArg {
                    assertFalse(
                        it.peekContent()
                    )
                })
                showLoadingObserver.onChanged(withArg { assertTrue(it.peekContent()) })
                getForecastForCityUseCase.execute(city = city)
                showErrorObserver.onChanged(withArg {
                    assertTrue(
                        it.peekContent()
                    )
                })
                showLoadingObserver.onChanged(withArg { assertFalse(it.peekContent()) })
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
            val formattedLocation = "$latitude ° N, $longitude ° E"
            val dayWeather1 = mockk<DayWeather>()
            val dayWeather2 = mockk<DayWeather>()
            val dayWeather3 = mockk<DayWeather>()
            val dayWeather4 = mockk<DayWeather>()
            val dayWeather5 = mockk<DayWeather>()
            val forecast = mockk<Forecast>() {
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

            coVerifySequence {
                showErrorObserver.onChanged(withArg {
                    assertFalse(
                        it.peekContent()
                    )
                })
                showLoadingObserver.onChanged(withArg { assertTrue(it.peekContent()) })
                getForecastForLocationUseCase.execute(latitude, longitude)
                weatherLocationNameObserver.onChanged(withArg {
                    assertEquals(
                        formattedLocation,
                        it
                    )
                })
                day1ForecastObserver.onChanged(withArg { assertEquals(listOf(dayWeather1), it) })
                day2ForecastObserver.onChanged(withArg { assertEquals(listOf(dayWeather2), it) })
                day3ForecastObserver.onChanged(withArg { assertEquals(listOf(dayWeather3), it) })
                day4ForecastObserver.onChanged(withArg { assertEquals(listOf(dayWeather4), it) })
                day5ForecastObserver.onChanged(withArg { assertEquals(listOf(dayWeather5), it) })

                showLoadingObserver.onChanged(withArg { assertFalse(it.peekContent()) })
                showLocationErrorObserver.onChanged(withArg {
                    assertFalse(
                        it.peekContent()
                    )
                })
            }
        }

        @Test
        fun `when use case is NOT successful, then notify correct observers`() {
            coEvery {
                getForecastForLocationUseCase.execute(
                    latitude = latitude,
                    longitude = longitude
                )
            } throws TestException

            sut.showForecastForCurrentLocation(latitude = latitude, longitude = longitude)

            coVerifySequence {
                showErrorObserver.onChanged(withArg {
                    assertFalse(
                        it.peekContent()
                    )
                })
                showLoadingObserver.onChanged(withArg { assertTrue(it.peekContent()) })
                getForecastForLocationUseCase.execute(latitude, longitude)
                showErrorObserver.onChanged(withArg {
                    assertTrue(
                        it.peekContent()
                    )
                })
                showLoadingObserver.onChanged(withArg { assertFalse(it.peekContent()) })
            }
        }

        @Test
        fun `when latitude and longitude are null, then notify correct observers`() {
            sut.showForecastForCurrentLocation(latitude = null, longitude = null)

            coVerifySequence {
                showLocationErrorObserver.onChanged(withArg {
                    assertTrue(
                        it.peekContent()
                    )
                })
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
            sut.navigateToMapFragment(latitude, longitude)
            coVerifySequence {
                navigateToMapObserver.onChanged(withArg {
                    assertEquals(
                        Pair(latitude.toFloat(), longitude.toFloat()),
                        it.peekContent()
                    )
                })
                showLocationErrorObserver.onChanged(withArg {
                    assertFalse(
                        it.peekContent()
                    )
                })
            }
        }

        @Test
        fun `when latitude and longitude are null, then notify howLocationError observer`() {
            sut.navigateToMapFragment(latitude = null, longitude = null)

            coVerifySequence {
                showLocationErrorObserver.onChanged(withArg {
                    assertTrue(
                        it.peekContent()
                    )
                })
            }
        }
    }
}

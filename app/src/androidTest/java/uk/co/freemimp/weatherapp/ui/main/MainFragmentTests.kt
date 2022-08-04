package uk.co.freemimp.weatherapp.ui.main

import android.location.Location
import androidx.test.core.app.launchActivity
import androidx.test.rule.GrantPermissionRule
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.adevinta.android.barista.interaction.BaristaEditTextInteractions.writeTo
import com.adevinta.android.barista.interaction.BaristaSleepInteractions.sleep
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import uk.co.freemimp.core.location.LocationRepository
import uk.co.freemimp.weatherapp.MainActivity
import uk.co.freemimp.weatherapp.R
import uk.co.freemimp.data.di.ViewModelModule
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import uk.co.freemimp.core.model.DayWeather
import uk.co.freemimp.core.model.Forecast
import uk.co.freemimp.core.repository.ForecastRepository
import uk.co.freemimp.data.ForecastRepositoryImpl
import uk.co.freemimp.weatherapp.LocationModule

@UninstallModules(ViewModelModule::class, LocationModule::class)
@HiltAndroidTest
class MainFragmentTests {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun givenApiCallSucceeds_whenGettingForecastForCity_thenForecastLocationNameAndForecastIsDisplayed() {
        launchActivity<MainActivity>().use {
            writeTo(R.id.cityForForecast, "London")

            clickOn(R.id.getForecast)

            assertDisplayed(R.id.weatherLocationName)
            assertDisplayed(R.id.day1Forecast)
            assertDisplayed(R.id.day2Forecast)
            assertDisplayed(R.id.day3Forecast)
            assertDisplayed(R.id.day4Forecast)
            assertDisplayed(R.id.day5Forecast)
        }
    }

    @Test
    fun givenApiCallSucceeds_whenGettingForecastForLocation_thenForecastLocationNameAndForecastIsDisplayed() {
        launchActivity<MainActivity>().use {
            sleep(5, TimeUnit.SECONDS)
            clickOn(R.id.getForecastForLocation)

            assertDisplayed(R.id.weatherLocationName)
            assertDisplayed(R.id.day1Forecast)
            assertDisplayed(R.id.day2Forecast)
            assertDisplayed(R.id.day3Forecast)
            assertDisplayed(R.id.day4Forecast)
            assertDisplayed(R.id.day5Forecast)
        }
    }

    @Test
    fun givenLocationIsEnabled_whenNavigatingToMap_thenMapIsDisplayed() {
        launchActivity<MainActivity>().use {
            sleep(5, TimeUnit.SECONDS)
            clickOn(R.id.openMapForLocation)

            assertDisplayed(R.id.map)
        }
    }

    @Module
    @InstallIn(SingletonComponent::class)
    object MockKViewModelModule {

        @Singleton
        @Provides
        fun provideMockkForecastRepository(): ForecastRepository {
            val dayWeather = mockk<DayWeather>(relaxed = true) {
                every { this@mockk.date } returns "2022-05-16"
                every { this@mockk.time } returns "18:00:00"
                every { this@mockk.temperature } returns 10.25
                every { this@mockk.iconUrl } returns "https://openweathermap.org/img/wn/01d@2x.png"
            }
            val forecast = mockk<Forecast> {
                every { this@mockk.day1 } returns listOf(dayWeather)
                every { this@mockk.day2 } returns listOf(dayWeather)
                every { this@mockk.day3 } returns listOf(dayWeather)
                every { this@mockk.day4 } returns listOf(dayWeather)
                every { this@mockk.day5 } returns listOf(dayWeather)
            }
            val impl = mockk<ForecastRepositoryImpl>()
            coEvery { impl.get5Day3HourForecastForCity(any()) } returns forecast
            coEvery { impl.get5Day3HourForecastForCurrentLocation(any(), any()) } returns forecast
            return impl
        }

        @Singleton
        @Provides
        fun provideMockkLocationRepository(): LocationRepository {
            val location = mockk<Location>(relaxed = true)
            val locationRepository = mockk<LocationRepository>()
            every { locationRepository.getLocation() } returns flowOf(location)

            return locationRepository
        }
    }
}

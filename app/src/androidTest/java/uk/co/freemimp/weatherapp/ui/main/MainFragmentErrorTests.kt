package uk.co.freemimp.weatherapp.ui.main

import android.content.Context
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
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import uk.co.freemimp.core.location.LocationRepository
import uk.co.freemimp.weatherapp.MainActivity
import uk.co.freemimp.weatherapp.R
import uk.co.freemimp.data.di.ViewModelModule
import uk.co.freemimp.weatherapp.util.TestException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import uk.co.freemimp.core.repository.ForecastRepository
import uk.co.freemimp.data.ForecastRepositoryImpl
import uk.co.freemimp.data.di.DataModule
import uk.co.freemimp.data.location.LocationRepositoryImpl
import uk.co.freemimp.data.location.SharedLocationManager
import uk.co.freemimp.weatherapp.LocationModule

@UninstallModules(ViewModelModule::class, LocationModule::class)
@HiltAndroidTest
class MainFragmentErrorTests {

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
    fun givenApiCallIsNotSuccessful_whenGettingForecastForCity_thenErrorIsDisplayed() {
        launchActivity<MainActivity>().use {
            writeTo(R.id.cityForForecast, "Lon")

            clickOn(R.id.getForecast)

            assertDisplayed(R.string.error_title)
            assertDisplayed(R.string.error_message)
        }
    }

    @Test
    fun givenApiCallIsNotSuccessful_whenGettingForecastForLocation_thenErrorIsDisplayed() {
        launchActivity<MainActivity>().use {
            clickOn(R.id.getForecastForLocation)

            assertDisplayed(R.string.error_title)
            assertDisplayed(R.string.error_message)
        }
    }

    @Module
    @InstallIn(SingletonComponent::class)
    object MockKModule {
        @Provides
        fun provideMockkForecastRepository(): ForecastRepository {
            val impl = mockk<ForecastRepositoryImpl>()
            coEvery { impl.get5Day3HourForecastForCity(any()) } throws TestException
            coEvery {
                impl.get5Day3HourForecastForCurrentLocation(
                    any(),
                    any()
                )
            } throws TestException
            return impl
        }

        @Provides
        fun provideMockkLocationRepository(): LocationRepository {
            val locationRepository = mockk<LocationRepository>()
            every { locationRepository.getLocations() } returns flowOf(mockk(relaxed = true))

            return locationRepository
        }
    }
}

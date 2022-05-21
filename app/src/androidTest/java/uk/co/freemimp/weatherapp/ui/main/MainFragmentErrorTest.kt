package uk.co.freemimp.weatherapp.ui.main

import androidx.test.core.app.launchActivity
import androidx.test.rule.GrantPermissionRule
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.adevinta.android.barista.interaction.BaristaEditTextInteractions.writeTo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import uk.co.freemimp.weatherapp.MainActivity
import uk.co.freemimp.weatherapp.R
import uk.co.freemimp.weatherapp.di.ViewModelModule
import uk.co.freemimp.weatherapp.domain.repository.ForecastRepository
import uk.co.freemimp.weatherapp.domain.repository.ForecastRepositoryImpl
import uk.co.freemimp.weatherapp.util.TestException
import javax.inject.Singleton

@UninstallModules(ViewModelModule::class)
@HiltAndroidTest
class MainFragmentErrorTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.ACCESS_COARSE_LOCATION
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
    object MockKViewModelModule {

        @Singleton
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
    }
}
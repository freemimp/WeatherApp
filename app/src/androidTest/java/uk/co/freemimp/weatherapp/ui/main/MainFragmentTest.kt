package uk.co.freemimp.weatherapp.ui.main

import androidx.test.core.app.launchActivity
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.adevinta.android.barista.interaction.BaristaEditTextInteractions.writeTo
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import uk.co.freemimp.weatherapp.MainActivity
import uk.co.freemimp.weatherapp.R

@HiltAndroidTest
class MainFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

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
}

package uk.co.freemimp.weatherapp.domain.usecase

import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import uk.co.freemimp.weatherapp.domain.model.Forecast
import uk.co.freemimp.weatherapp.domain.repository.ForecastRepository
import uk.co.freemimp.weatherapp.utils.TestException

@ExtendWith(MockKExtension::class)
internal class GetForecastForLocationUseCaseTest {
    @MockK
    private lateinit var repository: ForecastRepository

    @InjectMockKs
    private lateinit var sut: GetForecastForLocationUseCase

    private val latitude = 0.0
    private val longitude = 1.0

    @Test
    fun `given execute is invoked, when repository call is successful, then return forecast`() {
        runBlocking {
            val forecast = mockk<Forecast>()
            coEvery {
                repository.get5Day3HourForecastForCurrentLocation(
                    latitude = latitude, longitude = longitude
                )
            } returns forecast

            val result = sut.execute(latitude = latitude, longitude = longitude)

            assertEquals(forecast, result)
        }
    }

    @Test
    fun `given execute is invoked, when repository call is NOT successful, then throw exception`() {
        runBlocking {
            coEvery {
                repository.get5Day3HourForecastForCurrentLocation(
                    latitude = latitude,
                    longitude = longitude
                )
            } throws TestException

            assertThrows<TestException> { sut.execute(latitude = latitude, longitude = longitude) }
        }
    }
}

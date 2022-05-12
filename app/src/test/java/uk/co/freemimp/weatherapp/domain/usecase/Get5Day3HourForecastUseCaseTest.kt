package uk.co.freemimp.weatherapp.domain.usecase

import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions.*
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import uk.co.freemimp.weatherapp.domain.model.Forecast
import uk.co.freemimp.weatherapp.domain.repository.ForecastRepository
import uk.co.freemimp.weatherapp.utils.TestException

@ExtendWith(MockKExtension::class)
internal class Get5Day3HourForecastUseCaseTest {

    @MockK
    private lateinit var repository: ForecastRepository
    @InjectMockKs
    private lateinit var sut: Get5Day3HourForecastUseCase

    @Test
    fun `given get5Day3HourForecast is invoked, when repository call is successful, then return forecast`() {
        runBlocking {
            val forecast = mockk<Forecast>()
            coEvery { repository.get5Day3HourForecast(CITY) } returns forecast

            val result = sut.execute(CITY)

            assertEquals(forecast, result)
        }
    }

    @Test
    fun `given get5Day3HourForecast is invoked, when repository call is NOT successful, then throw exception`() {
        runBlocking {
            coEvery { repository.get5Day3HourForecast(CITY) } throws TestException

           assertThrows<TestException> { sut.execute(CITY) }
        }
    }
}

private const val CITY = "London"

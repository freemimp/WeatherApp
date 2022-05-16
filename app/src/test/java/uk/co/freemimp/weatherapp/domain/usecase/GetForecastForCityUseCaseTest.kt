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
internal class GetForecastForCityUseCaseTest {

    @MockK
    private lateinit var repository: ForecastRepository

    @InjectMockKs
    private lateinit var sut: GetForecastForCityUseCase

    private val city = "London"

    @Test
    fun `given execute is invoked, when repository call is successful, then return forecast`() {
        runBlocking {
            val forecast = mockk<Forecast>()
            coEvery { repository.get5Day3HourForecastForCity(city = city) } returns forecast

            val result = sut.execute(city = city)

            assertEquals(forecast, result)
        }
    }

    @Test
    fun `given execute is invoked, when repository call is NOT successful, then throw exception`() {
        runBlocking {
            coEvery { repository.get5Day3HourForecastForCity(city = city) } throws TestException

            assertThrows<TestException> { sut.execute(city = city) }
        }
    }
}

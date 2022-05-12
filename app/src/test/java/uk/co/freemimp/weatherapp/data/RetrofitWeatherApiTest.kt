package uk.co.freemimp.weatherapp.data

import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import retrofit2.HttpException
import retrofit2.Response
import uk.co.freemimp.weatherapp.data.model.WeatherForecastResponse
import uk.co.freemimp.weatherapp.domain.mapper.ForecastMapper
import uk.co.freemimp.weatherapp.domain.model.Forecast
import uk.co.freemimp.weatherapp.utils.TestException

@ExtendWith(MockKExtension::class)
internal class RetrofitWeatherApiTest {
    @RelaxedMockK
    private lateinit var service: WeatherService
    @RelaxedMockK
    private lateinit var mapper: ForecastMapper
    @InjectMockKs
    private lateinit var sut: RetrofitWeatherApi

    @RelaxedMockK
    private lateinit var response: Response<WeatherForecastResponse>
    @RelaxedMockK
    private lateinit var weatherForecastResponse: WeatherForecastResponse
    @RelaxedMockK
    private lateinit var forecast: Forecast

    @Nested
    @DisplayName("given getWeatherForecastForCity is invoked, ")
    inner class GetWeatherForecastForCity {

        @Test
        fun `when service call and mapping is successful, then return forecast `() {
            runBlocking {
                val city = "London"
                every { response.isSuccessful } returns true
                every { response.body() } returns weatherForecastResponse
                coEvery { service.getWeatherForecastForCity(city, "metric", any()) } returns response
                every { mapper.map(weatherForecastResponse) } returns forecast

                val result = sut.getWeatherData(city)
                val expected = forecast

                assertEquals(expected, result)
            }
        }

        @Test
        fun `when service call is not successful, then throw http exception `() {
            runBlocking {
                val city = "London"
                every { response.isSuccessful } returns false

                coEvery { service.getWeatherForecastForCity(city, "metric", any()) } returns response

                assertThrows<HttpException> { sut.getWeatherData(city) }
            }
        }

        @Test
        fun `when service call throws exception, then throw exception `() {
            runBlocking {
                val city = "London"
                coEvery { service.getWeatherForecastForCity(city, "metric", any()) } throws TestException

                assertThrows<TestException> { sut.getWeatherData(city) }
            }
        }
    }
}

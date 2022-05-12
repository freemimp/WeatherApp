package uk.co.freemimp.weatherapp.data

import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import uk.co.freemimp.weatherapp.utils.JsonResourseToStringMapper

@ExtendWith(MockKExtension::class)
internal class WeatherServiceTest {
    private lateinit var sut: WeatherService

    private lateinit var server: MockWebServer

    private val response =
        JsonResourseToStringMapper.getJsonStringFromFile("response.json")

    @BeforeEach
    fun setUp() {
        server = MockWebServer()
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(response)
        )
        server.start()
        sut = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(WeatherService::class.java)

    }

    @AfterEach
    fun tearDown() {
        server.shutdown()
    }

    @Nested
    @DisplayName("given getWeatherForecastForCity is executed")
    inner class GetWeatherForecastForCity {

        private val city = "London"
        private val units = "metric"
        private val apiKey = "supersecretapikey"
        @Test
        fun `then api call is made with correct path`() {
            runBlocking {
                sut.getWeatherForecastForCity(city = city, units = units, apiKey = apiKey)

                val expected = "/data/2.5/forecast?q=London&units=metric&appid=supersecretapikey"
                val actual = server.takeRequest().path

                assertEquals(expected, actual)
            }
        }

        @Test
        fun `then api call is made with correct http method`() {
            runBlocking {
                sut.getWeatherForecastForCity(city = city, units = units, apiKey = apiKey)

                val expected = "GET"
                val actual = server.takeRequest().method

                assertEquals(expected, actual)
            }
        }
    }
}

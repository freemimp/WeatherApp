package uk.co.freemimp.data

import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import uk.co.freemimp.commontest.utils.JsonResourseToStringMapper

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

        @Test
        fun `then the response is correctly parsed`() {
            runBlocking {
                sut.getWeatherForecastForCity(city = city, units = units, apiKey = apiKey)

                // This will throw no error if it was parsed successfully
                server.takeRequest()
            }
        }
    }

    @Nested
    @DisplayName("given getWeatherForecastForLocation is executed")
    inner class GetWeatherForecastForLocation {

        private val lat = 0.0
        private val lon = 0.0
        private val units = "metric"
        private val apiKey = "supersecretapikey"

        @Test
        fun `then api call is made with correct path`() {
            runBlocking {
                sut.getWeatherForecastForLocation(
                    latitude = lat,
                    longitude = lon,
                    units = units,
                    apiKey = apiKey
                )

                val expected =
                    "/data/2.5/forecast?lat=0.0&lon=0.0&units=metric&appid=supersecretapikey"
                val actual = server.takeRequest().path

                assertEquals(expected, actual)
            }
        }

        @Test
        fun `then api call is made with correct http method`() {
            runBlocking {
                sut.getWeatherForecastForLocation(
                    latitude = lat,
                    longitude = lon,
                    units = units,
                    apiKey = apiKey
                )

                val expected = "GET"
                val actual = server.takeRequest().method

                assertEquals(expected, actual)
            }
        }

        @Test
        fun `then the response is correctly parsed`() {
            runBlocking {
                sut.getWeatherForecastForLocation(
                    latitude = lat,
                    longitude = lon,
                    units = units,
                    apiKey = apiKey
                )

                // This will throw no error if it was parsed successfully
                server.takeRequest()
            }
        }
    }
}

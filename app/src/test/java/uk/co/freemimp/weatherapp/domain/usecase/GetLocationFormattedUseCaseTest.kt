package uk.co.freemimp.weatherapp.domain.usecase

import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
@DisplayName("given execute is invoked, ")
internal class GetLocationFormattedUseCaseTest {

    @InjectMockKs
    private lateinit var sut: GetLocationFormattedUseCase

    @Test
    fun `when latitude and longitude are bigger then zero, then format correctly to 'N and 'E postfixes`() {
        val latitude = 1.0
        val longitude = 2.0

        val result = sut.execute(latitude = latitude, longitude = longitude)
        val expected = "1.0000 ° N, 2.0000 ° E"

        assertEquals(expected, result)
    }

    @Test
    fun `when latitude and longitude are less then zero, then format correctly to 'S and 'W postfixes`() {
        val latitude = -1.0
        val longitude = -2.0

        val result = sut.execute(latitude = latitude, longitude = longitude)
        val expected = "1.0000 ° S, 2.0000 ° W"

        assertEquals(expected, result)
    }

    @Test
    fun `when latitude and longitude are zero, then format correctly to 'N and 'E postfixes`() {
        val latitude = 0.0
        val longitude = 0.0

        val result = sut.execute(latitude = latitude, longitude = longitude)
        val expected = "0.0000 ° N, 0.0000 ° E"

        assertEquals(expected, result)
    }

    @Test
    fun `when latitude and longitude with high precision, then format correctly to values with 4 digit precision with correct rounding`() {
        val latitude = 59.01233545439
        val longitude = -0.13409865098

        val result = sut.execute(latitude = latitude, longitude = longitude)
        val expected = "59.0123 ° N, 0.1341 ° W"

        assertEquals(expected, result)
    }
}

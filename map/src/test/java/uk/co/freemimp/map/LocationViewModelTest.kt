package uk.co.freemimp.map

import android.location.Location
import app.cash.turbine.test
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import uk.co.freemimp.core.location.LocationRepository

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
internal class LocationViewModelTest {

    @RelaxedMockK
    private lateinit var locationRepository: LocationRepository

    private lateinit var sut: LocationViewModel

    @RelaxedMockK
    private lateinit var location: Location
    private val locationFlow = MutableStateFlow<Location>(mockk())

    @Test
    fun `given sut created, when location repository emits location, then sut emits location`() {
        runTest {
            every { location.latitude } returns COORDINATE
            every { location.longitude } returns COORDINATE
            every { locationRepository.getLocation() } returns locationFlow

            locationFlow.emit(location)

            instantiateSut()

            sut.location.test {
                assertEquals(location, awaitItem())
                cancelAndConsumeRemainingEvents()
            }
        }
    }

    private fun instantiateSut() {
        sut = LocationViewModel(locationRepository)
    }
}

private const val COORDINATE = 0.0

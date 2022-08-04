package uk.co.freemimp.data.location

import android.location.Location
import app.cash.turbine.test
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
internal class LocationRepositoryImplTest {
    @MockK
    private lateinit var locationManager: SharedLocationManager

    @InjectMockKs
    private lateinit var sut: LocationRepositoryImpl

    @RelaxedMockK
    private lateinit var location: Location
    private val locationFlow = MutableStateFlow<Location>(mockk())

    @Test
    fun `given location manager emits location, then sut emits location`() {
        runTest {
            every { location.latitude } returns COORDINATE
            every { location.longitude } returns COORDINATE
            every { locationManager.locationFlow() } returns locationFlow

            locationFlow.emit(location)

            sut.getLocation().test {
                assertEquals(location, awaitItem())
            }
        }
    }
}

private const val COORDINATE = 0.0

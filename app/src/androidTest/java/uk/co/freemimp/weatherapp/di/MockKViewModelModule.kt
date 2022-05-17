package uk.co.freemimp.weatherapp.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import uk.co.freemimp.weatherapp.domain.model.DayWeather
import uk.co.freemimp.weatherapp.domain.model.Forecast
import uk.co.freemimp.weatherapp.domain.repository.ForecastRepository
import uk.co.freemimp.weatherapp.domain.repository.ForecastRepositoryImpl
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ViewModelModule::class]
)
object MockKViewModelModule {

    @Singleton
    @Provides
     fun provideMockkForecastRepository(): ForecastRepository {
        val dayWeather = mockk<DayWeather> {
            every { date } returns "2022-05-16"
            every { time } returns "18:00:00"
            every { temperature } returns 10.25
            every { iconUrl } returns "https://openweathermap.org/img/wn/01d@2x.png"
        }
        val forecast = mockk<Forecast> {
            every { day1 } returns listOf(dayWeather)
            every { day2 } returns listOf(dayWeather)
            every { day3 } returns listOf(dayWeather)
            every { day4 } returns listOf(dayWeather)
            every { day5 } returns listOf(dayWeather)
        }
        val impl = mockk<ForecastRepositoryImpl>()
        coEvery { impl.get5Day3HourForecastForCity(any()) } returns forecast
        coEvery { impl.get5Day3HourForecastForCurrentLocation(any(), any()) } returns forecast
        return impl
     }
}

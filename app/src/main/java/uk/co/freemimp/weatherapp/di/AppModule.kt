package uk.co.freemimp.weatherapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import uk.co.freemimp.weatherapp.data.RetrofitWeatherApi
import uk.co.freemimp.weatherapp.domain.WeatherApi
import uk.co.freemimp.weatherapp.domain.repository.ForecastRepository
import uk.co.freemimp.weatherapp.domain.repository.ForecastRepositoryImpl

@Module
@InstallIn(ActivityComponent::class)
abstract class AppModule {

    @Binds
    abstract fun bindWeatherApi(retrofitWeatherApi: RetrofitWeatherApi): WeatherApi

    @Binds
    abstract fun bindForecastRepository(impl: ForecastRepositoryImpl): ForecastRepository
}

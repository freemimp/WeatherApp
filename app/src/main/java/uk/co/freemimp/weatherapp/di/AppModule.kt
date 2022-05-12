package uk.co.freemimp.weatherapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uk.co.freemimp.weatherapp.data.RetrofitWeatherApi
import uk.co.freemimp.weatherapp.domain.WeatherApi

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppModule {

    @Binds
    abstract fun bindWeatherApi(retrofitWeatherApi: RetrofitWeatherApi): WeatherApi
}

package uk.co.freemimp.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uk.co.freemimp.data.RetrofitWeatherApi
import uk.co.freemimp.data.WeatherApi


@Module
@InstallIn(ViewModelComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindWeatherApi(retrofitWeatherApi: RetrofitWeatherApi): WeatherApi
}

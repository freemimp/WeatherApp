package uk.co.freemimp.data.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import uk.co.freemimp.core.location.LocationRepository
import uk.co.freemimp.data.RetrofitWeatherApi
import uk.co.freemimp.data.WeatherApi
import uk.co.freemimp.data.location.LocationRepositoryImpl
import uk.co.freemimp.data.location.SharedLocationManager
import javax.inject.Singleton


@Module
@InstallIn(ViewModelComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindWeatherApi(retrofitWeatherApi: RetrofitWeatherApi): WeatherApi

    @Binds
    abstract fun bindLocationRepository(impl: LocationRepositoryImpl): LocationRepository
}

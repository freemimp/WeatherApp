package uk.co.freemimp.weatherapp

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import uk.co.freemimp.core.location.LocationRepository
import uk.co.freemimp.data.location.LocationRepositoryImpl
import uk.co.freemimp.data.location.SharedLocationManager
import javax.inject.Singleton

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
object LocationModule {
    @Provides
    @Singleton
    fun provideSharedLocationManager(
        @ApplicationContext context: Context
    ): SharedLocationManager =
        SharedLocationManager(context, (context.applicationContext as WeatherApp).applicationScope)

    @Provides
    @Singleton
    fun bindLocationRepository(impl: LocationRepositoryImpl): LocationRepository = impl
}

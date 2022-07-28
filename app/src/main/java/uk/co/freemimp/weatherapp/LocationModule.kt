package uk.co.freemimp.weatherapp

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import uk.co.freemimp.data.location.SharedLocationManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {
    @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
    @Provides
    @Singleton
    fun provideSharedLocationManager(
        @ApplicationContext context: Context
    ): SharedLocationManager =
        SharedLocationManager(context, (context.applicationContext as WeatherApp).applicationScope)

}

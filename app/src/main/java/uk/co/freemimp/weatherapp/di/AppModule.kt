package uk.co.freemimp.weatherapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import uk.co.freemimp.weatherapp.BuildConfig
import uk.co.freemimp.weatherapp.data.WeatherService

@Module
@InstallIn(ActivityComponent::class)
object AppModule {

    @Provides
    fun provideAnalyticsService(
        okHttpClient: OkHttpClient
    ): WeatherService {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.BASE_URL)
            .build()
            .create(WeatherService::class.java)
    }

    @Provides
    fun provideOkhttpClient(
        interceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    @Provides
    fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }
}
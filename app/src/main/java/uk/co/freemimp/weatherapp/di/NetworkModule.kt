package uk.co.freemimp.weatherapp.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import uk.co.freemimp.weatherapp.BuildConfig
import uk.co.freemimp.weatherapp.data.WeatherService

@Module
@InstallIn(ViewModelComponent::class)
object NetworkModule {

    @Provides
    fun provideWeatherService(
        okHttpClient: OkHttpClient
    ): WeatherService {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
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

    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }
}

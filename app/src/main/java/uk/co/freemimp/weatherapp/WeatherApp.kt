package uk.co.freemimp.weatherapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope

@HiltAndroidApp
class WeatherApp: Application() {
    @DelicateCoroutinesApi
    val applicationScope = GlobalScope
}

package uk.co.freemimp.weatherapp.ui.main

import androidx.recyclerview.widget.DiffUtil
import uk.co.freemimp.core.model.DayWeather


object DayWeatherDiffUtilItemCallback : DiffUtil.ItemCallback<DayWeather>() {
    override fun areItemsTheSame(
        oldItem: DayWeather,
        newItem: DayWeather
    ): Boolean {
        return oldItem.javaClass == newItem.javaClass
    }

    override fun areContentsTheSame(
        oldItem: DayWeather,
        newItem: DayWeather
    ): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(
        oldItem: DayWeather,
        newItem: DayWeather
    ): Any {
        return newItem
    }
}

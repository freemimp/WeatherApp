package uk.co.freemimp.weatherapp.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uk.co.freemimp.weatherapp.databinding.ForecastItemBinding
import uk.co.freemimp.weatherapp.domain.model.DayWeather

class ForecastItemAdapter : ListAdapter<DayWeather, RecyclerView.ViewHolder>(
    DayWeatherDiffUtilItemCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ForecastItemViewHolder(
            ForecastItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        bindViewHolder(holder, getItem(position))
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            bindViewHolder(holder, getItem(position))
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    private fun bindViewHolder(
        holder: RecyclerView.ViewHolder,
        dayWeather: DayWeather
    ) {
        (holder as ForecastItemViewHolder).bind(dayWeather)
    }

    inner class ForecastItemViewHolder(private val binding: ForecastItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dayWeather: DayWeather) {
            binding.forecastTime.text = dayWeather.time
            binding.forecastTemperature.text = dayWeather.temperature.toString()
            Glide.with(binding.root).load(dayWeather.iconUrl).into(binding.forecastIcon)
        }
    }
}

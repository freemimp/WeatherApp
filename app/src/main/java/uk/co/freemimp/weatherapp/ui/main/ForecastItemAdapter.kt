package uk.co.freemimp.weatherapp.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import uk.co.freemimp.core.model.DayWeather
import uk.co.freemimp.weatherapp.R
import uk.co.freemimp.weatherapp.databinding.ForecastItemBinding


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

        private val context = binding.root.context
        private val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)

        fun bind(dayWeather: DayWeather) {
            binding.forecastTime.text = context.getString(R.string.time, dayWeather.time)
            binding.forecastTemperature.text =
                context.getString(R.string.temperature, dayWeather.temperature)

            Glide.with(binding.root).load(dayWeather.iconUrl).apply(requestOptions)
                .into(binding.forecastIcon)
        }
    }
}

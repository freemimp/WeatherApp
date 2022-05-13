package uk.co.freemimp.weatherapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import uk.co.freemimp.weatherapp.R
import uk.co.freemimp.weatherapp.databinding.FragmentMainBinding
import uk.co.freemimp.weatherapp.mvvm.EventObserver

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    private var _binding: FragmentMainBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "Cannot access binding outside of onCreateView & onDestroyView as it will be null"
        }

    private val day1Adapter = ForecastItemAdapter()
    private val day2Adapter = ForecastItemAdapter()
    private val day3Adapter = ForecastItemAdapter()
    private val day4Adapter = ForecastItemAdapter()
    private val day5Adapter = ForecastItemAdapter()

    private var errorDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()

        binding.getForecast.setOnClickListener {
            viewModel.showForecastForTheCity(binding.cityForForecast.text.toString())
        }

        viewModel.showLoading.observe(viewLifecycleOwner, EventObserver {
            binding.loading.isVisible = it
        })

        viewModel.showError.observe(viewLifecycleOwner, EventObserver {
            showErrorDialog(R.string.error_title, R.string.error_message)
        })

        viewModel.day1Forecast.observe(viewLifecycleOwner) { weather ->
            binding.day1Forecast.dayDate.text = weather.first().date
            day1Adapter.submitList(weather)
        }
        viewModel.day2Forecast.observe(viewLifecycleOwner) { weather ->
            binding.day2Forecast.dayDate.text = weather.first().date
            day2Adapter.submitList(weather)
        }
        viewModel.day3Forecast.observe(viewLifecycleOwner) { weather ->
            binding.day3Forecast.dayDate.text = weather.first().date
            day3Adapter.submitList(weather)
        }
        viewModel.day4Forecast.observe(viewLifecycleOwner) { weather ->
            binding.day4Forecast.dayDate.text = weather.first().date
            day4Adapter.submitList(weather)
        }
        viewModel.day5Forecast.observe(viewLifecycleOwner) { weather ->
            binding.day5Forecast.dayDate.text = weather.first().date
            day5Adapter.submitList(weather)
        }
    }

    private fun setupRecyclerViews() {
        binding.day1Forecast.dayHourlyForecast.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = day1Adapter
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.HORIZONTAL))
        }
        binding.day2Forecast.dayHourlyForecast.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = day2Adapter
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.HORIZONTAL))
        }
        binding.day3Forecast.dayHourlyForecast.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = day3Adapter
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.HORIZONTAL))
        }
        binding.day4Forecast.dayHourlyForecast.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = day4Adapter
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.HORIZONTAL))
        }
        binding.day5Forecast.dayHourlyForecast.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = day5Adapter
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.HORIZONTAL))
        }
    }

    private fun showErrorDialog(title: Int, message: Int) {
        errorDialog?.dismiss()
        (errorDialog ?: AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
            .create()
            .also { errorDialog = it })
            .show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

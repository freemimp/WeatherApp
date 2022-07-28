package uk.co.freemimp.weatherapp.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import uk.co.freemimp.weatherapp.R
import uk.co.freemimp.weatherapp.databinding.FragmentMainBinding

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
    private var locationErrorDialog: AlertDialog? = null

    private lateinit var locationPermissionRequest: ActivityResultLauncher<String>

    private var location: Location? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationPermissionRequest =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    getCurrentLocation()
                } else {
                    showLocationRationaleToast()
                }
            }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        setupButtons()
        setupObservables()
        checkAndAskForPermission()
    }

    private fun showLocationRationaleToast() {
        Toast.makeText(
            requireContext(),
            "Need location permission to show weather for your location",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun checkAndAskForPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                getCurrentLocation()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                showLocationRationaleToast()
                locationPermissionRequest.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
            else -> {
                locationPermissionRequest.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getLocation.collect {
                location = it
            }
        }
    }

    private fun setupButtons() {
        binding.getForecast.setOnClickListener {
            viewModel.showForecastForTheCity(binding.cityForForecast.text.toString())
        }
        binding.getForecastForLocation.setOnClickListener {
            viewModel.showForecastForCurrentLocation(location?.latitude, location?.longitude)
        }
        binding.openMapForLocation.setOnClickListener {
            viewModel.navigateToMapFragment(location?.latitude, location?.longitude)
        }
    }

    private fun setupObservables() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.navigateToMap.collect {
                val action = MainFragmentDirections.actionMainFragmentToLocationFragment(
                    it.first,
                    it.second
                )
                findNavController().navigate(action)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.showLocationError.collect {
                if (it) {
                    showLocationErrorDialog(
                        R.string.location_error_title,
                        R.string.location_error_message
                    )
                } else {
                    locationErrorDialog?.dismiss()
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.showLoading.collect {
                binding.loading.isVisible = it
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.showError.collect {
                if (it) {
                    showErrorDialog(R.string.error_title, R.string.error_message)
                } else {
                    errorDialog?.dismiss()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.weatherLocationName.collect {
                binding.weatherLocationName.text = getString(R.string.weather_location_name, it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.day1Forecast.collect { weather ->
                if (weather.isNotEmpty()) {
                    binding.day1Forecast.dayDate.text =
                        getString(R.string.forecast_date, weather.first().date)
                    day1Adapter.submitList(weather)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.day2Forecast.collect { weather ->
                if (weather.isNotEmpty()) {
                    binding.day2Forecast.dayDate.text =
                        getString(R.string.forecast_date, weather.first().date)
                    day2Adapter.submitList(weather)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.day3Forecast.collect { weather ->
                if (weather.isNotEmpty()) {
                    binding.day3Forecast.dayDate.text =
                        getString(R.string.forecast_date, weather.first().date)
                    day3Adapter.submitList(weather)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.day4Forecast.collect { weather ->
                if (weather.isNotEmpty()) {
                    binding.day4Forecast.dayDate.text =
                        getString(R.string.forecast_date, weather.first().date)
                    day4Adapter.submitList(weather)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.day5Forecast.collect { weather ->
                if (weather.isNotEmpty()) {
                    binding.day5Forecast.dayDate.text =
                        getString(R.string.forecast_date, weather.first().date)
                    day5Adapter.submitList(weather)
                }
            }
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

    private fun showLocationErrorDialog(title: Int, message: Int) {
        locationErrorDialog?.dismiss()
        (locationErrorDialog ?: AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
            .create()
            .also { locationErrorDialog = it })
            .show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

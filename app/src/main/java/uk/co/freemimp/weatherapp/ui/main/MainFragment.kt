package uk.co.freemimp.weatherapp.ui.main

import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
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

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var location: Location? = null

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
        setAndAskForLocation()
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    this.location = location
                }
            }
        setupButtons()
        setupObservables()
    }

    private fun setAndAskForLocation() {
        val locationPermissionRequest =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    fusedLocationClient =
                        LocationServices.getFusedLocationProviderClient(requireContext())
                } else {
                    showLocationToast()
                }
            }
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                fusedLocationClient =
                    LocationServices.getFusedLocationProviderClient(requireContext())
            }
            shouldShowRequestPermissionRationale("Coarse Location permission") -> {
                showLocationToast()
            }
            else -> {
                locationPermissionRequest.launch(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            }
        }
    }

   private fun createLocationRequest() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(requireContext())
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(requireContext())
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException){
                try {
                    exception.startResolutionForResult(requireActivity(),
                        REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    private fun showLocationToast() {
        Toast.makeText(
            requireContext(),
            "Need location permission to show weather for your location",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun setupButtons() {
        binding.getForecastForLocation.setOnClickListener {
            createLocationRequest()
            if (location != null) {
                location?.let {
                    viewModel.showForecastForCurrentLocation(it.latitude, it.longitude)
                }
            } else {
                showErrorDialog(R.string.location_error_title, R.string.location_error_message)
            }
        }

        binding.getForecast.setOnClickListener {
            viewModel.showForecastForTheCity(binding.cityForForecast.text.toString())
        }
    }

    private fun setupObservables() {
        viewModel.showLoading.observe(viewLifecycleOwner, EventObserver {
            binding.loading.isVisible = it
        })

        viewModel.showError.observe(viewLifecycleOwner, EventObserver {
            showErrorDialog(R.string.error_title, R.string.error_message)
        })

        viewModel.weatherLocationName.observe(viewLifecycleOwner) {
            binding.weatherLocationName.text = getString(R.string.weather_location_name, it)
        }

        viewModel.day1Forecast.observe(viewLifecycleOwner) { weather ->
            binding.day1Forecast.dayDate.text =
                getString(R.string.forecast_date, weather.first().date)
            day1Adapter.submitList(weather)
        }
        viewModel.day2Forecast.observe(viewLifecycleOwner) { weather ->
            binding.day2Forecast.dayDate.text =
                getString(R.string.forecast_date, weather.first().date)
            day2Adapter.submitList(weather)
        }
        viewModel.day3Forecast.observe(viewLifecycleOwner) { weather ->
            binding.day3Forecast.dayDate.text =
                getString(R.string.forecast_date, weather.first().date)
            day3Adapter.submitList(weather)
        }
        viewModel.day4Forecast.observe(viewLifecycleOwner) { weather ->
            binding.day4Forecast.dayDate.text =
                getString(R.string.forecast_date, weather.first().date)
            day4Adapter.submitList(weather)
        }
        viewModel.day5Forecast.observe(viewLifecycleOwner) { weather ->
            binding.day5Forecast.dayDate.text =
                getString(R.string.forecast_date, weather.first().date)
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

private const val REQUEST_CHECK_SETTINGS = 479

package uk.co.freemimp.map

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uk.co.freemimp.map.databinding.FragmentLocationBinding

@AndroidEntryPoint
class LocationFragment : Fragment(), OnMapReadyCallback {

    private val viewModel: LocationViewModel by viewModels()

    private var _binding: FragmentLocationBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "Cannot access binding outside of onCreateView & onDestroyView as it will be null"
        }

    private var locationFlow: Job? = null
    private var location: Location? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationFlow = viewModel.location
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach {
               location = it
            }
            .launchIn(lifecycleScope)

        binding.map.getFragment<SupportMapFragment>().getMapAsync(this)
    }



    override fun onMapReady(googleMap: GoogleMap) {
        setupMap(googleMap, location)
    }

    private fun setupMap(googleMap: GoogleMap, location: Location?) {

        val userLocation = LatLng(location?.latitude ?: 0.0, location?.longitude ?: 0.0)
        val cameraPosition = CameraPosition.Builder()
            .target(userLocation)
            .zoom(DEFAULT_ZOOM)
            .build()
        googleMap.addMarker(
            MarkerOptions().position(userLocation)
                .title("YourLocation")
        )

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun onDestroyView() {
        locationFlow?.cancel()
        _binding = null
        super.onDestroyView()
    }
}

private const val DEFAULT_ZOOM = 12f

package uk.co.freemimp.weatherapp.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import uk.co.freemimp.weatherapp.databinding.FragmentLocationBinding

class LocationFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentLocationBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "Cannot access binding outside of onCreateView & onDestroyView as it will be null"
        }

    private val args: LocationFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.map.getFragment<SupportMapFragment>().getMapAsync(this)
    }
    override fun onMapReady(googleMap: GoogleMap) {
        val userLocation = LatLng(args.latitude.toDouble(), args.longitude.toDouble())
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
        _binding = null
        super.onDestroyView()
    }
}

private const val DEFAULT_ZOOM = 12f

package uk.co.freemimp.weatherapp.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import uk.co.freemimp.weatherapp.R
import uk.co.freemimp.weatherapp.databinding.FragmentMainBinding
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    private var _binding: FragmentMainBinding? = null
    private val binding
    get() = requireNotNull(_binding) {
        "Cannot access binding outside of onCreateView & onDestroyView as it will be null"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.showForecastForTheCity("London")

        viewModel.day1Forecast.observe(viewLifecycleOwner) { weather ->
            binding.message.text = weather.joinToString { it.temperature.toString() }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

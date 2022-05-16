package uk.co.freemimp.weatherapp.domain.usecase

import javax.inject.Inject
import kotlin.math.absoluteValue

class GetLocationFormattedUseCase @Inject constructor() {
    fun execute(latitude: Double, longitude: Double): String {
        val latitudePostfix = if (latitude >= 0) {
            "\u00B0 N"
        } else {
            "\u00B0 S"
        }

        val longitudePostfix = if (longitude >= 0) {
            "\u00B0 E"
        } else {
            "\u00B0 W"
        }

        return "${String.format("%.4f", latitude.absoluteValue)} $latitudePostfix, ${
            String.format(
                "%.4f",
                longitude.absoluteValue
            )
        } $longitudePostfix"
    }
}

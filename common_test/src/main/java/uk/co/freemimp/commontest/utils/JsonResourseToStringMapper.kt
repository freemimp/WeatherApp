package uk.co.freemimp.commontest.utils

import java.io.InputStreamReader

object JsonResourseToStringMapper {
    fun getJsonStringFromFile(path: String) = InputStreamReader(
        requireNotNull(this::class.java.classLoader).getResourceAsStream(path)
    ).readText()
}

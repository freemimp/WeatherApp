/*
 * Copyright 2021 Sean Barbeau
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.co.freemimp.data.location

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import uk.co.freemimp.core.location.LocationRepository
import javax.inject.Inject

@ExperimentalCoroutinesApi
class LocationRepositoryImpl @Inject constructor(
        private val sharedLocationManager: SharedLocationManager
) : LocationRepository {
    /**
     * Observable flow for location updates
     */
    override fun getLocations() = sharedLocationManager.locationFlow()
}

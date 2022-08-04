### Weather app

Location repository is derived from https://github.com/barbeau/while-in-use-location/tree/no-database

#### Please note
In order for google maps to work you need to add `MAPS_API_KEY=your_google_maps_api_key` in to `local.properties`

#### Work done
- Architecture MVVM with coroutines and flow
- App is split in to data, domain and separate ui modules, core module contains classes used by other modules, 
- common-test contains common test utilities
- using Hilt, Moshi and Jetpack Navigation, Mockk, JUnit5 among other things
- Unit and UI tests

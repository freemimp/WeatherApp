package uk.co.freemimp.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uk.co.freemimp.core.repository.ForecastRepository
import uk.co.freemimp.data.ForecastRepositoryImpl


@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {

    @Binds
    abstract fun bindForecastRepository(impl: ForecastRepositoryImpl): ForecastRepository
}

package io.github.fvrodas.core

import io.github.fvrodas.core.data.repositories.RemoteSeriesRepository
import io.github.fvrodas.core.domain.repositories.ISeriesRepository
import io.github.fvrodas.core.domain.usecases.GetListOfSeriesByPageNumberUseCase
import io.github.fvrodas.core.domain.usecases.GetSeriesDetailByIdUseCase
import io.github.fvrodas.core.domain.usecases.SearchSeriesByNameUseCase
import org.koin.dsl.module

var coreModule = module {
    single<ISeriesRepository> { RemoteSeriesRepository() }
    single {GetListOfSeriesByPageNumberUseCase(get())}
    single {SearchSeriesByNameUseCase(get())}
    single {GetSeriesDetailByIdUseCase(get())}
}
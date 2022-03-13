package io.github.fvrodas.core

import io.github.fvrodas.core.data.repositories.RemoteShowsRepository
import io.github.fvrodas.core.domain.repositories.IShowsRepository
import io.github.fvrodas.core.domain.usecases.GetListOfShowsByPageNumberUseCase
import io.github.fvrodas.core.domain.usecases.GetShowDetailsByIdUseCase
import io.github.fvrodas.core.domain.usecases.SearchShowByNameUseCase
import org.koin.dsl.module

val coreModule = module {
    single<IShowsRepository> { RemoteShowsRepository() }
    single {GetListOfShowsByPageNumberUseCase(get())}
    single {SearchShowByNameUseCase(get())}
    single {GetShowDetailsByIdUseCase(get())}
}
package io.github.fvrodas.core

import io.github.fvrodas.core.common.db.DatabaseFactory
import io.github.fvrodas.core.data.datasources.FavoriteShowsLocalDataSource
import io.github.fvrodas.core.data.repositories.FavoriteShowsRepository
import io.github.fvrodas.core.data.repositories.RemotePeopleRepository
import io.github.fvrodas.core.data.repositories.RemoteShowsRepository
import io.github.fvrodas.core.domain.repositories.IFavoriteShowsRepository
import io.github.fvrodas.core.domain.repositories.IPeopleRepository
import io.github.fvrodas.core.domain.repositories.IShowsRepository
import io.github.fvrodas.core.domain.usecases.*
import org.koin.dsl.module

val coreModule = module {
    single { DatabaseFactory(get()) }
    single { FavoriteShowsLocalDataSource(get()) }
    single<IShowsRepository> { RemoteShowsRepository() }
    single<IFavoriteShowsRepository> { FavoriteShowsRepository(get()) }
    single<IPeopleRepository> { RemotePeopleRepository() }
    single { GetListOfShowsByPageNumberUseCase(get()) }
    single { SearchShowByNameUseCase(get()) }
    single { GetShowDetailsByIdUseCase(get()) }
    single { AddFavoriteShowUseCase(get()) }
    single { DeleteFavoriteShowUseCase(get()) }
    single { GetListOfFavoriteShowsUseCase(get()) }
    single { IsFavoriteShowUseCase(get()) }
    single { GetListOfPeopleByPageNumberUseCase(get()) }
    single { SearchPeopleByNameUseCase(get()) }
    single { GetCastCreditsByIdUseCase(get()) }
    single { GetCrewCreditsByIdUseCase(get()) }
}
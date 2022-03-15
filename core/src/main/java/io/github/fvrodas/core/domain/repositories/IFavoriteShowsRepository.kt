package io.github.fvrodas.core.domain.repositories

import io.github.fvrodas.core.domain.entities.ShowEntity

interface IFavoriteShowsRepository {
    suspend fun getFavoriteShows() : Result<List<ShowEntity>>
    suspend fun addFavoriteShow(show: ShowEntity) : Result<ShowEntity>
    suspend fun deleteFavoriteShow(show: ShowEntity) : Result<ShowEntity>
}
package io.github.fvrodas.core.data.datasources

import io.github.fvrodas.core.common.db.DatabaseFactory
import io.github.fvrodas.core.domain.entities.ShowEntity

class FavoriteShowsLocalDataSource(private val databaseFactory: DatabaseFactory) {
    fun getFavoriteShows(): List<ShowEntity> = databaseFactory.db.favoriteShowsDao().getAll()
    fun addFavoriteShow(show: ShowEntity) = databaseFactory.db.favoriteShowsDao().insertAll(show)
    fun deleteShow(show: ShowEntity) = databaseFactory.db.favoriteShowsDao().delete(show)
    fun isFavorite(showId: Long) = databaseFactory.db.favoriteShowsDao().isFavorite(showId)
}
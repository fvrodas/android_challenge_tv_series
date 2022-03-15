package io.github.fvrodas.core.data.repositories

import io.github.fvrodas.core.data.datasources.FavoriteShowsLocalDataSource
import io.github.fvrodas.core.domain.entities.ShowEntity
import io.github.fvrodas.core.domain.repositories.IFavoriteShowsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavoriteShowsRepository(
    private val localDataSource: FavoriteShowsLocalDataSource,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IFavoriteShowsRepository {
    override suspend fun getFavoriteShows(): Result<List<ShowEntity>> {
        return withContext(coroutineDispatcher) {
            try {
                return@withContext Result.success(localDataSource.getFavoriteShows())
            } catch (e: Exception) {
                return@withContext Result.failure(e)
            }
        }
    }

    override suspend fun addFavoriteShow(show: ShowEntity): Result<ShowEntity> {
        return withContext(coroutineDispatcher) {
            try {
                localDataSource.addFavoriteShow(show)
                return@withContext Result.success(show)
            } catch (e: Exception) {
                return@withContext Result.failure(e)
            }
        }
    }

    override suspend fun deleteFavoriteShow(show: ShowEntity): Result<ShowEntity> {
        return withContext(coroutineDispatcher) {
            try {
                localDataSource.deleteShow(show)
                return@withContext Result.success(show)
            } catch (e: Exception) {
                return@withContext Result.failure(e)
            }
        }
    }

    override suspend fun isFavorite(show: ShowEntity): Result<Boolean> {
        return withContext(coroutineDispatcher) {
            try {
                val count = localDataSource.isFavorite(show.id)
                return@withContext Result.success(count > 0)
            } catch (e: Exception) {
                return@withContext Result.failure(e)
            }
        }
    }

}
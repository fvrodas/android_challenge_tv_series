package io.github.fvrodas.core.data.repositories

import android.util.Log
import io.github.fvrodas.core.BuildConfig
import io.github.fvrodas.core.common.network.TvMazeApi
import io.github.fvrodas.core.domain.entities.ShowEntity
import io.github.fvrodas.core.domain.repositories.IShowsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteShowsRepository(
    private val dataSource: TvMazeApi = TvMazeApi.services,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IShowsRepository {

    override suspend fun getSeriesListByPageNumber(pageNumber: Long): Result<List<ShowEntity>> {
        return withContext(coroutineDispatcher) {
            try {
                return@withContext Result.success(
                    dataSource.getListOfShowsByPageNumber(pageNumber).map { it.asEntity() })
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) {
                    Log.d(this::class.java.canonicalName, e.message ?: "")
                }
                return@withContext Result.failure(e)
            }
        }
    }

    override suspend fun searchSeriesByName(name: String): Result<List<ShowEntity>> {
        return withContext(coroutineDispatcher) {
            try {
                return@withContext Result.success(
                    dataSource.searchShowsByName(name).map { it.show.asEntity() })
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) {
                    Log.d(this::class.java.canonicalName, e.message ?: "")
                }
                return@withContext Result.failure(e)
            }
        }
    }

    override suspend fun getSeriesDetailByID(ID: Long): Result<ShowEntity> {
        return withContext(coroutineDispatcher) {
            try {
                return@withContext Result.success(dataSource.getShowDetailsById(ID).asEntity())
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) {
                    Log.d(this::class.java.canonicalName, e.message ?: "")
                }
                return@withContext Result.failure(e)
            }
        }
    }
}
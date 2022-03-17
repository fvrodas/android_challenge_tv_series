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
                return@withContext if (showsCache.containsKey(pageNumber) && showsCache[pageNumber] != null) {
                    Result.success(showsCache[pageNumber]!!)
                } else {
                    showsCache[pageNumber] =
                        dataSource.getListOfShowsByPageNumber(pageNumber).map { it.asEntity() }
                    Result.success(showsCache[pageNumber]!!)
                }
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
                return@withContext if (showDetailsCache.containsKey(ID) && showDetailsCache.isNotEmpty()) {
                    Result.success(showDetailsCache[ID]!!)
                } else {
                    showDetailsCache[ID] = dataSource.getShowDetailsById(ID).asEntity()
                    Result.success(showDetailsCache[ID]!!)
                }
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) {
                    Log.d(this::class.java.canonicalName, e.message ?: "")
                }
                return@withContext Result.failure(e)
            }
        }
    }
}

private val showsCache: HashMap<Long, List<ShowEntity>> = HashMap()
private val showDetailsCache: HashMap<Long, ShowEntity> = HashMap()
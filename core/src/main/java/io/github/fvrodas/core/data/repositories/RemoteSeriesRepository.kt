package io.github.fvrodas.core.data.repositories

import io.github.fvrodas.core.common.network.TvMazeApi
import io.github.fvrodas.core.domain.entities.SeriesEntity
import io.github.fvrodas.core.domain.repositories.ISeriesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteSeriesRepository(
    private val dataSource: TvMazeApi = TvMazeApi.services,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ISeriesRepository {

    override suspend fun getSeriesListByPageNumber(pageNumber: Long): Result<List<SeriesEntity>> {
        return withContext(coroutineDispatcher) {
            try {
                return@withContext Result.success(
                    dataSource.getListOfSeriesByPageNumber(pageNumber).map { it.asEntity() })
            } catch (e: Exception) {
                return@withContext Result.failure(e)
            }
        }
    }

    override suspend fun searchSeriesByName(name: String): Result<List<SeriesEntity>> {
        return withContext(coroutineDispatcher) {
            try {
                return@withContext Result.success(
                    dataSource.searchSeriesByName(name).map { it.asEntity() })
            } catch (e: Exception) {
                return@withContext Result.failure(e)
            }
        }
    }

    override suspend fun getSeriesDetailByID(ID: Long): Result<SeriesEntity> {
        return withContext(coroutineDispatcher) {
            try {
                return@withContext Result.success(dataSource.getSeriesDetailById(ID).asEntity())
            } catch (e: Exception) {
                return@withContext Result.failure(e)
            }
        }
    }
}
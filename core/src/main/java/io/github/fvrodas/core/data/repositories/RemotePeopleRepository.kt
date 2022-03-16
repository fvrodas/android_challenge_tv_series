package io.github.fvrodas.core.data.repositories

import android.util.Log
import io.github.fvrodas.core.BuildConfig
import io.github.fvrodas.core.common.network.TvMazeApi
import io.github.fvrodas.core.domain.entities.CrewCreditEntity
import io.github.fvrodas.core.domain.entities.PersonEntity
import io.github.fvrodas.core.domain.repositories.IPeopleRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemotePeopleRepository(
    private val dataSource: TvMazeApi = TvMazeApi.services,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IPeopleRepository {

    override suspend fun getPeopleListByPageNumber(pageNumber: Long): Result<List<PersonEntity>> {
        return withContext(coroutineDispatcher) {
            try {
                return@withContext Result.success(
                    dataSource.getListOfPeopleByPageNumber(pageNumber).map { it.asEntity() })
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) {
                    Log.d(this::class.java.canonicalName, e.message ?: "")
                }
                return@withContext Result.failure(e)
            }
        }
    }

    override suspend fun searchPeopleByName(name: String): Result<List<PersonEntity>> {
        return withContext(coroutineDispatcher) {
            try {
                return@withContext Result.success(
                    dataSource.searchPeopleByName(name).map { it.person.asEntity() })
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) {
                    Log.d(this::class.java.canonicalName, e.message ?: "")
                }
                return@withContext Result.failure(e)
            }
        }
    }

    override suspend fun getCastCredits(personId: Long): Result<List<CrewCreditEntity>> {
        return withContext(coroutineDispatcher) {
            try {
                return@withContext Result.success(
                    dataSource.getCastCreditsById(personId).map { it.asEntity() }
                )
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) {
                    Log.d(this::class.java.canonicalName, e.message ?: "")
                }
                return@withContext Result.failure(e)
            }
        }
    }

    override suspend fun getCrewCredits(personId: Long): Result<List<CrewCreditEntity>> {
        return withContext(coroutineDispatcher) {
            try {
                return@withContext Result.success(
                    dataSource.getCrewCreditsById(personId).map { it.asEntity() }
                )
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) {
                    Log.d(this::class.java.canonicalName, e.message ?: "")
                }
                return@withContext Result.failure(e)
            }
        }
    }
}
package io.github.fvrodas.core.domain.usecases

import io.github.fvrodas.core.common.usecases.UseCase
import io.github.fvrodas.core.domain.entities.SeriesEntity
import io.github.fvrodas.core.domain.repositories.ISeriesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetSeriesDetailByIdUseCase(
    val repository: ISeriesRepository,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UseCase<SeriesEntity, Long>() {

    override suspend fun invoke(params: Long): Result<SeriesEntity> =
        withContext(coroutineDispatcher) {
            return@withContext repository.getSeriesDetailByID(params)
        }
}
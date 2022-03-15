package io.github.fvrodas.core.domain.usecases

import io.github.fvrodas.core.common.usecases.UseCase
import io.github.fvrodas.core.domain.entities.ShowEntity
import io.github.fvrodas.core.domain.repositories.IShowsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetShowDetailsByIdUseCase(
    private val repository: IShowsRepository,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UseCase<ShowEntity, Long>() {

    override suspend fun invoke(params: Long): Result<ShowEntity> =
        withContext(coroutineDispatcher) {
            return@withContext repository.getSeriesDetailByID(params)
        }
}
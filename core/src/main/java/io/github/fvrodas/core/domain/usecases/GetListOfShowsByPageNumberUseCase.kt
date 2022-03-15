package io.github.fvrodas.core.domain.usecases

import io.github.fvrodas.core.common.usecases.UseCase
import io.github.fvrodas.core.domain.entities.ShowEntity
import io.github.fvrodas.core.domain.repositories.IShowsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetListOfShowsByPageNumberUseCase(
    private val repository: IShowsRepository,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UseCase<List<ShowEntity>, Long>() {

    override suspend fun invoke(params: Long): Result<List<ShowEntity>> =
        withContext(coroutineDispatcher) {
            return@withContext repository.getSeriesListByPageNumber(params)
        }
}
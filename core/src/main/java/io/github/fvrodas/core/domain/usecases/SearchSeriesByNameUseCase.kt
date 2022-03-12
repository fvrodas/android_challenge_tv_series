package io.github.fvrodas.core.domain.usecases

import io.github.fvrodas.core.common.usecases.UseCase
import io.github.fvrodas.core.domain.entities.SeriesEntity
import io.github.fvrodas.core.domain.repositories.ISeriesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchSeriesByNameUseCase(
    val repository: ISeriesRepository,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UseCase<List<SeriesEntity>, String>() {

    override suspend fun invoke(params: String): Result<List<SeriesEntity>> =
        withContext(coroutineDispatcher) {
            return@withContext repository.searchSeriesByName(params)
        }
}
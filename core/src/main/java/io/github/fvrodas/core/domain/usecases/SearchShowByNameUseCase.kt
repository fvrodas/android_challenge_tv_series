package io.github.fvrodas.core.domain.usecases

import io.github.fvrodas.core.common.usecases.UseCase
import io.github.fvrodas.core.domain.entities.ShowEntity
import io.github.fvrodas.core.domain.repositories.IShowsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchShowByNameUseCase(
    private val repository: IShowsRepository,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UseCase<List<ShowEntity>, String>() {

    override suspend fun invoke(params: String): Result<List<ShowEntity>> =
        withContext(coroutineDispatcher) {
            return@withContext repository.searchSeriesByName(params)
        }
}
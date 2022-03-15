package io.github.fvrodas.core.domain.usecases

import io.github.fvrodas.core.common.usecases.UseCase
import io.github.fvrodas.core.domain.entities.ShowEntity
import io.github.fvrodas.core.domain.repositories.IFavoriteShowsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetListOfFavoriteShowsUseCase(
    private val repository: IFavoriteShowsRepository,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UseCase<List<ShowEntity>, Nothing?>() {

    override suspend fun invoke(params: Nothing?): Result<List<ShowEntity>> =
        withContext(coroutineDispatcher) {
            return@withContext repository.getFavoriteShows()
        }
}
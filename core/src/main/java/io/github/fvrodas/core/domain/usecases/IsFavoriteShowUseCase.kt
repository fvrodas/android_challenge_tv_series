package io.github.fvrodas.core.domain.usecases

import io.github.fvrodas.core.common.usecases.UseCase
import io.github.fvrodas.core.domain.entities.ShowEntity
import io.github.fvrodas.core.domain.repositories.IFavoriteShowsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class IsFavoriteShowUseCase(
    private val repository: IFavoriteShowsRepository,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UseCase<Boolean, ShowEntity>() {

    override suspend fun invoke(params: ShowEntity): Result<Boolean> =
        withContext(coroutineDispatcher) {
            return@withContext repository.isFavorite(params)
        }

}
package io.github.fvrodas.core.domain.usecases

import io.github.fvrodas.core.common.usecases.UseCase
import io.github.fvrodas.core.domain.entities.ShowEntity
import io.github.fvrodas.core.domain.repositories.IFavoriteShowsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddFavoriteShowUseCase(
    private val repository: IFavoriteShowsRepository,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UseCase<ShowEntity, ShowEntity>() {

    override suspend fun invoke(params: ShowEntity): Result<ShowEntity> =
        withContext(coroutineDispatcher) {
            return@withContext repository.addFavoriteShow(params)
        }

}
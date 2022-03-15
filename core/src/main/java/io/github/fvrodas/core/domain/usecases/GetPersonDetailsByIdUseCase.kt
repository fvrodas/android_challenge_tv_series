package io.github.fvrodas.core.domain.usecases

import io.github.fvrodas.core.common.usecases.UseCase
import io.github.fvrodas.core.domain.entities.PersonEntity
import io.github.fvrodas.core.domain.repositories.IPeopleRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetPersonDetailsByIdUseCase(
    private val repository: IPeopleRepository,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UseCase<PersonEntity, Long>() {

    override suspend fun invoke(params: Long): Result<PersonEntity> =
        withContext(coroutineDispatcher) {
            return@withContext repository.getPersonDetails(params)
        }
}
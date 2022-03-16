package io.github.fvrodas.core.domain.usecases

import io.github.fvrodas.core.common.usecases.UseCase
import io.github.fvrodas.core.domain.entities.CrewCreditEntity
import io.github.fvrodas.core.domain.repositories.IPeopleRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetCrewCreditsByIdUseCase(
    private val repository: IPeopleRepository,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UseCase<List<CrewCreditEntity>, Long>() {

    override suspend fun invoke(params: Long): Result<List<CrewCreditEntity>> =
        withContext(coroutineDispatcher) {
            return@withContext repository.getCrewCredits(params)
        }
}
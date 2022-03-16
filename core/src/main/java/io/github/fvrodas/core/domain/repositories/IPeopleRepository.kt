package io.github.fvrodas.core.domain.repositories

import io.github.fvrodas.core.domain.entities.CrewCreditEntity
import io.github.fvrodas.core.domain.entities.PersonEntity

interface IPeopleRepository {
    suspend fun getPeopleListByPageNumber(pageNumber: Long): Result<List<PersonEntity>>
    suspend fun searchPeopleByName(name: String): Result<List<PersonEntity>>
    suspend fun getCastCredits(personId: Long) : Result<List<CrewCreditEntity>>
    suspend fun getCrewCredits(personId: Long) : Result<List<CrewCreditEntity>>
}
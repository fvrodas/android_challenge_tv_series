package io.github.fvrodas.core.domain.repositories

import io.github.fvrodas.core.domain.entities.PersonEntity

interface IPeopleRepository {
    suspend fun getPeopleListByPageNumber(pageNumber: Long): Result<List<PersonEntity>>
    suspend fun searchPeopleByName(name: String): Result<List<PersonEntity>>
    suspend fun getPersonDetails(personId: Long) : Result<PersonEntity>
}
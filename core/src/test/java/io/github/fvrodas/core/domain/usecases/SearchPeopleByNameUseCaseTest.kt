package io.github.fvrodas.core.domain.usecases

import io.github.fvrodas.core.domain.entities.PersonEntity
import io.github.fvrodas.core.domain.repositories.IPeopleRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SearchPeopleByNameUseCaseTest {
    @Mock
    private lateinit var repository: IPeopleRepository;

    private lateinit var useCase: SearchPeopleByNameUseCase

    private val expectedResult: List<PersonEntity> = listOf(
        PersonEntity(
            id = 1,
            name = "Test Person",
            gender = "Male",
            image = null,
            imageHQ = null,
            shows = null
        )
    )

    @Test
    fun searchPeopleByName_ShouldReturnListOfPersonEntityIfSucceed() {

        runBlocking {
            useCase = SearchPeopleByNameUseCase(repository)

            Mockito.`when`(repository.searchPeopleByName(anyString())).thenReturn(
                Result.success(
                    expectedResult
                )
            )
            val result = useCase("Test")

            assert(result.isSuccess)
            assert(result.getOrNull() == expectedResult)
        }
    }
}
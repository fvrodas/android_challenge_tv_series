package io.github.fvrodas.core.domain.usecases

import io.github.fvrodas.core.domain.entities.PersonEntity
import io.github.fvrodas.core.domain.repositories.IPeopleRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetPeopleDetailsByIdUseCaseTest {
    @Mock
    private lateinit var repository: IPeopleRepository;

    private lateinit var useCase: GetPersonDetailsByIdUseCase

    private val expectedResult: PersonEntity = PersonEntity(
        id = 1,
        name = "Test Person",
        gender = "Male",
        image = null
    )


    @Test
    fun getPersonDetailsById_ShouldReturnPersonEntityIfSucceed() {

        runBlocking {
            useCase = GetPersonDetailsByIdUseCase(repository)

            Mockito.`when`(repository.getPersonDetails(anyLong())).thenReturn(
                Result.success(
                    expectedResult
                )
            )
            val result = useCase(1)

            assert(result.isSuccess)
            assert(result.getOrNull() == expectedResult)
        }
    }
}
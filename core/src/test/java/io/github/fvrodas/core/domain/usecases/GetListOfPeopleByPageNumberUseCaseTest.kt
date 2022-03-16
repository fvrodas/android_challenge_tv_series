package io.github.fvrodas.core.domain.usecases

import io.github.fvrodas.core.domain.entities.PersonEntity
import io.github.fvrodas.core.domain.repositories.IPeopleRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyLong
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetListOfPeopleByPageNumberUseCaseTest {

    @Mock
    private lateinit var repository: IPeopleRepository;

    private lateinit var useCase: GetListOfPeopleByPageNumberUseCase

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
    fun getListOfPeopleByPageNumber_ShouldReturnListOfPeopleIfSucceed() {

        runBlocking {
            useCase = GetListOfPeopleByPageNumberUseCase(repository)

            `when`(repository.getPeopleListByPageNumber(anyLong())).thenReturn(
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
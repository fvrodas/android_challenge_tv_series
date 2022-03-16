package io.github.fvrodas.core.domain.usecases

import io.github.fvrodas.core.data.models.Schedule
import io.github.fvrodas.core.domain.entities.CrewCreditEntity
import io.github.fvrodas.core.domain.entities.ShowEntity
import io.github.fvrodas.core.domain.repositories.IPeopleRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetCrewCreditsByIdUseCaseTest {
    @Mock
    private lateinit var repository: IPeopleRepository

    private lateinit var useCase: GetCrewCreditsByIdUseCase

    private val expectedResult: List<CrewCreditEntity> = listOf(
        CrewCreditEntity(
            type = "Director",
            show = ShowEntity(
                id = 1,
                name = "Test Series",
                url = "https://testurl.com/-1",
                poster = "https://testurl.com/-1/image.jpg",
                posterHQ = "https://testurl.com/-1/image.jpg",
                summary = "Test Summary",
                genres = listOf("horror", "sci-fi"),
                status = "ended",
                schedule = Schedule(time = "16:00", days = listOf("friday")),
                null
            )
        )
    )


    @Test
    fun getCrewCreditsById_ShouldReturnListOfCrewCreditsEntityIfSucceed() {

        runBlocking {
            useCase = GetCrewCreditsByIdUseCase(repository)

            Mockito.`when`(repository.getCrewCredits(anyLong())).thenReturn(
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
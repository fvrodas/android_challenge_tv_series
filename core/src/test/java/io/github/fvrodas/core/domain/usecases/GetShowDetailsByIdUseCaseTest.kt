package io.github.fvrodas.core.domain.usecases

import io.github.fvrodas.core.data.models.Schedule
import io.github.fvrodas.core.domain.entities.ShowEntity
import io.github.fvrodas.core.domain.repositories.IShowsRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetShowDetailsByIdUseCaseTest {
    @Mock
    private lateinit var repository: IShowsRepository;

    private lateinit var useCase: GetShowDetailsByIdUseCase

    private val expectedResult: ShowEntity = ShowEntity(
        id = 1,
        name = "Test Series",
        url = "https://testurl.com/-1",
        poster = "https://testurl.com/-1/image.jpg",
        posterHQ = "https://testurl.com/-1/image.jpg",
        summary = "Test Summary",
        genres = listOf("horror", "scifi"),
        status = "ended",
        schedule = Schedule(time = "16:00", days = listOf("friday")),
        null
    )


    @Test
    fun getShowDetailsById_ShouldReturnShow() {

        runBlocking {
            useCase = GetShowDetailsByIdUseCase(repository)

            Mockito.`when`(repository.getSeriesDetailByID(anyLong())).thenReturn(
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
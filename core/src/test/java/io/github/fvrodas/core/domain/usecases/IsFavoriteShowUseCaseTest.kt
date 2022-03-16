package io.github.fvrodas.core.domain.usecases

import io.github.fvrodas.core.data.models.Schedule
import io.github.fvrodas.core.domain.entities.ShowEntity
import io.github.fvrodas.core.domain.repositories.IFavoriteShowsRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class IsFavoriteShowUseCaseTest {
    @Mock
    private lateinit var repository: IFavoriteShowsRepository

    private lateinit var useCase: IsFavoriteShowUseCase

    private val expectedResult = true

    private val param: ShowEntity =
        ShowEntity(
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

    @Test
    fun isFavoriteShow_shouldReturnBooleanIfSucceed() {
        runBlocking {

            useCase = IsFavoriteShowUseCase(repository)

            `when`(repository.isFavorite(param)).thenReturn(
                Result.success(
                    expectedResult
                )
            )

            val result = useCase(param)

            assert(result.isSuccess)
            assert(expectedResult)
        }
    }
}
package io.github.fvrodas.core.domain.usecases

import io.github.fvrodas.core.data.models.Schedule
import io.github.fvrodas.core.domain.entities.ShowEntity
import io.github.fvrodas.core.domain.repositories.IFavoriteShowsRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DeleteFavoriteShowUseCaseTest {

    @Mock
    private lateinit var repository: IFavoriteShowsRepository

    private lateinit var useCase: DeleteFavoriteShowUseCase

    private val deletedShowResult =
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
    fun deleteFavoriteStory_shouldReturnStoryIfSucceed() {
        runBlocking {

            useCase = DeleteFavoriteShowUseCase(repository)

            Mockito.`when`(repository.deleteFavoriteShow(deletedShowResult)).thenReturn(
                Result.success(
                    deletedShowResult
                )
            )

            val result = useCase(deletedShowResult)

            assert(result.isSuccess)
            assert(deletedShowResult == result.getOrNull())
        }
    }
}
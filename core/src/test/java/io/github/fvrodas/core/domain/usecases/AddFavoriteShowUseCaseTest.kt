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
class AddFavoriteShowUseCaseTest {

    @Mock
    private lateinit var repository: IFavoriteShowsRepository

    private lateinit var useCase: AddFavoriteShowUseCase

    private val addedShowResult =
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
    fun addFavoriteShow_shouldReturnShowEntityIfSucceed() {
        runBlocking {

            useCase = AddFavoriteShowUseCase(repository)

            Mockito.`when`(repository.addFavoriteShow(addedShowResult)).thenReturn(
                Result.success(
                    addedShowResult
                )
            )

            val result = useCase(addedShowResult)

            assert(result.isSuccess)
            assert(addedShowResult == result.getOrNull())
        }
    }
}
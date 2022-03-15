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
class GetListOfFavoriteShowsUseCaseTest {
    @Mock
    private lateinit var repository: IFavoriteShowsRepository;

    private lateinit var useCase: GetListOfFavoriteShowsUseCase

    private val expectedResult: List<ShowEntity> = listOf(
        ShowEntity(
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
    )

    @Test
    fun getListOfFavoriteShows_ShouldReturnListOfShows() {

        runBlocking {
            useCase = GetListOfFavoriteShowsUseCase(repository)

            Mockito.`when`(repository.getFavoriteShows()).thenReturn(
                Result.success(
                    expectedResult
                )
            )
            val result = useCase(null)

            assert(result.isSuccess)
            assert(result.getOrNull() == expectedResult)
        }
    }
}
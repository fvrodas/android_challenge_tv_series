package io.github.fvrodas.core.domain.usecases

import io.github.fvrodas.core.data.models.Schedule
import io.github.fvrodas.core.domain.entities.ShowEntity
import io.github.fvrodas.core.domain.repositories.IShowsRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SearchShowByNameUseCaseTest {
    @Mock
    private lateinit var repository: IShowsRepository;

    private lateinit var useCase: SearchShowByNameUseCase

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
    fun searchShowsByName_ShouldReturnListOfShows() {

        runBlocking {
            useCase = SearchShowByNameUseCase(repository)

            Mockito.`when`(repository.searchSeriesByName(anyString())).thenReturn(
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
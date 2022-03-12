package io.github.fvrodas.core.domain.usecases

import io.github.fvrodas.core.data.models.Schedule
import io.github.fvrodas.core.domain.entities.SeriesEntity
import io.github.fvrodas.core.domain.repositories.ISeriesRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SearchSeriesByNameUseCaseTest {
    @Mock
    private lateinit var repository: ISeriesRepository;

    private lateinit var useCase: SearchSeriesByNameUseCase

    private val expectedResult: List<SeriesEntity> = listOf(
        SeriesEntity(
            id = 1,
            name = "Test Series",
            url = "https://testurl.com/-1",
            poster = "https://testurl.com/-1/image.jpg",
            posterHQ = "https://testurl.com/-1/image.jpg",
            summary = "Test Summary",
            genre = listOf("horror", "scifi"),
            status = "ended",
            schedule = Schedule(time = "16:00", days = listOf("friday")),
            null
        )
    )

    @Test
    fun getSeriesByName_ShouldReturnListOfSeries() {

        runBlocking {
            useCase = SearchSeriesByNameUseCase(repository)

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
package io.github.fvrodas.core.domain.usecases

import io.github.fvrodas.core.data.models.Schedule
import io.github.fvrodas.core.domain.entities.SeriesEntity
import io.github.fvrodas.core.domain.repositories.ISeriesRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyLong
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetListOfSeriesByPageNumberUseCaseTest {

    @Mock
    private lateinit var repository: ISeriesRepository;

    private lateinit var useCase: GetListOfSeriesByPageNumberUseCase

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
    fun getSeriesListByPageNumber_ShouldReturnListOfSeries() {

        runBlocking {
            useCase = GetListOfSeriesByPageNumberUseCase(repository)

            `when`(repository.getSeriesListByPageNumber(anyLong())).thenReturn(
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
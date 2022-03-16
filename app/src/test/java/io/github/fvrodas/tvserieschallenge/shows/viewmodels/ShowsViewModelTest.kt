package io.github.fvrodas.tvserieschallenge.shows.viewmodels

import io.github.fvrodas.core.data.models.Schedule
import io.github.fvrodas.core.domain.entities.ShowEntity
import io.github.fvrodas.core.domain.repositories.IShowsRepository
import io.github.fvrodas.core.domain.usecases.GetListOfShowsByPageNumberUseCase
import io.github.fvrodas.core.domain.usecases.SearchShowByNameUseCase
import io.github.fvrodas.tvserieschallenge.features.shows.viewmodels.ShowsUiState
import io.github.fvrodas.tvserieschallenge.features.shows.viewmodels.ShowsViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ShowsViewModelTest {

    @Mock
    private lateinit var repository: IShowsRepository

    private lateinit var getListOfShowsByPageNumberUseCase: GetListOfShowsByPageNumberUseCase
    private lateinit var searchShowsByNameUseCase: SearchShowByNameUseCase

    private lateinit var viewModel: ShowsViewModel

    private val testShow: ShowEntity = ShowEntity(
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

    private val testList = listOf(testShow)

    @Before
    fun setup() {
        getListOfShowsByPageNumberUseCase = GetListOfShowsByPageNumberUseCase(repository)
        searchShowsByNameUseCase = SearchShowByNameUseCase(repository)

        viewModel = ShowsViewModel(
            getListOfShowsByPageNumberUseCase,
            searchShowsByNameUseCase,
        )
    }

    @Test
    fun getListOfShows_shouldReturnListOfShowsEntity() {
        runBlocking {
            `when`(repository.getSeriesListByPageNumber(Mockito.anyLong())).thenReturn(
                Result.success(
                    testList
                )
            )

            val result: MutableList<ShowsUiState> = mutableListOf()

            val job = launch {
                viewModel.showsUiState.toList(result)
            }

            viewModel.retrieveCurrentPage()

            assert(result.first() is ShowsUiState.Loading)
            assert(result.last() is ShowsUiState.Success)

            job.cancel()
        }
    }

    @Test
    fun searchShowByName_shouldReturnListOfShowsEntity() {
        runBlocking {
            `when`(repository.searchSeriesByName(Mockito.anyString())).thenReturn(
                Result.success(
                    testList
                )
            )

            val result: MutableList<ShowsUiState> = mutableListOf()

            val job = launch {
                viewModel.showsUiState.toList(result)
            }

            viewModel.searchShowByName("X-Files")

            assert(result.first() is ShowsUiState.Loading)
            assert(result.last() is ShowsUiState.Success)

            job.cancel()
        }
    }


}
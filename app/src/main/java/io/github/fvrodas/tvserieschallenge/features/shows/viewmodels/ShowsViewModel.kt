package io.github.fvrodas.tvserieschallenge.features.shows.viewmodels

import androidx.lifecycle.ViewModel
import io.github.fvrodas.core.domain.entities.ShowEntity
import io.github.fvrodas.core.domain.usecases.GetListOfShowsByPageNumberUseCase
import io.github.fvrodas.core.domain.usecases.SearchShowByNameUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val PAGE_NONE : Long = -1

class ShowsViewModel(
    private val getListOfShowsByPageNumberUseCase: GetListOfShowsByPageNumberUseCase,
    private val searchShowByNameUseCase: SearchShowByNameUseCase,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    val showsUiState: MutableStateFlow<ShowsUiState> =
        MutableStateFlow(ShowsUiState.Loading)

    private var currentPage: Long = 0

    init {
        retrieveCurrentPage()
    }

    private fun retrieveShowsByPageNumber(page: Long) {
        CoroutineScope(coroutineDispatcher).launch {
            showsUiState.update { ShowsUiState.Loading }
            try {
                val result = getListOfShowsByPageNumberUseCase(page).getOrThrow()
                showsUiState.update { ShowsUiState.Success(result, page) }
            } catch (e: Exception) {
                showsUiState.update { ShowsUiState.Failure(e.localizedMessage ?: "") }
            }
        }
    }

    fun searchShowByName(showName: String) {
        CoroutineScope(coroutineDispatcher).launch {
            showsUiState.update { ShowsUiState.Loading }
            try {
                val result = searchShowByNameUseCase(showName).getOrThrow()
                showsUiState.update { ShowsUiState.Success(result, PAGE_NONE) }
            } catch (e: Exception) {
                showsUiState.update { ShowsUiState.Failure(e.localizedMessage ?: "") }
            }
        }
    }

    fun retrieveCurrentPage() {
        retrieveShowsByPageNumber(currentPage)
    }

    fun retrievePreviousPage() {
        if (currentPage - 1 >= 0) {
            currentPage--
            retrieveShowsByPageNumber(currentPage)
        }
    }

    fun retrieveNextPage() {
        currentPage++
        retrieveShowsByPageNumber(currentPage)
    }
}

sealed class ShowsUiState {
    object Loading : ShowsUiState()
    class Success(val shows: List<ShowEntity>, val pageNumber: Long) : ShowsUiState()
    class Failure(val error: String) : ShowsUiState()
}
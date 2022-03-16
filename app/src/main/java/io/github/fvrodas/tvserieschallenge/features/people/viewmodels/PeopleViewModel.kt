package io.github.fvrodas.tvserieschallenge.features.people.viewmodels

import androidx.lifecycle.ViewModel
import io.github.fvrodas.core.domain.entities.PersonEntity
import io.github.fvrodas.core.domain.usecases.GetListOfPeopleByPageNumberUseCase
import io.github.fvrodas.core.domain.usecases.SearchPeopleByNameUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val PAGE_NONE : Long = -1

class PeopleViewModel(
    private val getListOfPeopleByPageNumberUseCase: GetListOfPeopleByPageNumberUseCase,
    private val searchPeopleByNameUseCase: SearchPeopleByNameUseCase,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    val peopleUiState: MutableStateFlow<PeopleUiState> =
        MutableStateFlow(PeopleUiState.Loading)

    private var currentPage: Long = 0

    init {
        retrieveCurrentPage()
    }

    private fun retrieveShowsByPageNumber(page: Long) {
        CoroutineScope(coroutineDispatcher).launch {
            peopleUiState.update { PeopleUiState.Loading }
            try {
                val result = getListOfPeopleByPageNumberUseCase(page).getOrThrow()
                peopleUiState.update { PeopleUiState.Success(result, page) }
            } catch (e: Exception) {
                peopleUiState.update { PeopleUiState.Failure(e.localizedMessage ?: "") }
            }
        }
    }

    fun searchShowByName(showName: String) {
        CoroutineScope(coroutineDispatcher).launch {
            peopleUiState.update { PeopleUiState.Loading }
            try {
                val result = searchPeopleByNameUseCase(showName).getOrThrow()
                peopleUiState.update { PeopleUiState.Success(result, PAGE_NONE) }
            } catch (e: Exception) {
                peopleUiState.update { PeopleUiState.Failure(e.localizedMessage ?: "") }
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

sealed class PeopleUiState {
    object Loading : PeopleUiState()
    class Success(val people: List<PersonEntity>, val pageNumber: Long) : PeopleUiState()
    class Failure(val error: String) : PeopleUiState()
}
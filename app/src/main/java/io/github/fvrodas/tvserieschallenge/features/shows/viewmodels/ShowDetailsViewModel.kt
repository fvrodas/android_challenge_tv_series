package io.github.fvrodas.tvserieschallenge.features.shows.viewmodels

import androidx.lifecycle.ViewModel
import io.github.fvrodas.core.domain.entities.ShowEntity
import io.github.fvrodas.core.domain.usecases.AddFavoriteShowUseCase
import io.github.fvrodas.core.domain.usecases.GetShowDetailsByIdUseCase
import io.github.fvrodas.core.domain.usecases.IsFavoriteShowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ShowDetailsViewModel(
    private val getShowDetailsByIdUseCase: GetShowDetailsByIdUseCase,
    private val addFavoriteShowUseCase: AddFavoriteShowUseCase,
    private val isFavoriteShowUseCase: IsFavoriteShowUseCase,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    val showDetailsUiState: MutableStateFlow<ShowDetailsUiState> =
        MutableStateFlow(ShowDetailsUiState.Loading)

    fun retrieveShowDetailsById(showId: Long) {
        CoroutineScope(coroutineDispatcher).launch {
            showDetailsUiState.update { ShowDetailsUiState.Loading }
            try {
                val result = getShowDetailsByIdUseCase(showId).getOrThrow()
                val isFavorite = isFavoriteShowUseCase(result).getOrThrow()
                showDetailsUiState.update { ShowDetailsUiState.Success(result, isFavorite) }
            } catch (e: Exception) {
                showDetailsUiState.update { ShowDetailsUiState.Message(e.localizedMessage ?: "") }
            }
        }
    }

    fun addShowToFavorites(show: ShowEntity) {
        CoroutineScope(coroutineDispatcher).launch {
            showDetailsUiState.update { ShowDetailsUiState.Loading }
            try {
                val result = addFavoriteShowUseCase(show).getOrThrow()
                val isFavorite = isFavoriteShowUseCase(result).getOrThrow()
                showDetailsUiState.update { ShowDetailsUiState.Success(result, isFavorite) }
            } catch (e: Exception) {
                showDetailsUiState.update { ShowDetailsUiState.Message(e.localizedMessage ?: "") }
            }
        }
    }
}

sealed class ShowDetailsUiState {
    object Loading : ShowDetailsUiState()
    class Success(val shows: ShowEntity, val isFavorite: Boolean) : ShowDetailsUiState()
    class Message(val message: String) : ShowDetailsUiState()
}
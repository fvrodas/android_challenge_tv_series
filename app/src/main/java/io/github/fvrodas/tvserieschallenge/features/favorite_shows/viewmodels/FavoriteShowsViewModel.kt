package io.github.fvrodas.tvserieschallenge.features.favorite_shows.viewmodels

import androidx.lifecycle.ViewModel
import io.github.fvrodas.core.domain.entities.ShowEntity
import io.github.fvrodas.core.domain.usecases.DeleteFavoriteShowUseCase
import io.github.fvrodas.core.domain.usecases.GetListOfFavoriteShowsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class FavoriteShowsViewModel(
    private val getListOfFavoriteShowsUseCase: GetListOfFavoriteShowsUseCase,
    private val deleteFavoriteShowUseCase: DeleteFavoriteShowUseCase,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    val favoriteShowsUiState: MutableStateFlow<FavoriteShowsUiState> =
        MutableStateFlow(FavoriteShowsUiState.Loading)

    fun retrieveFavoriteShows() {
        CoroutineScope(coroutineDispatcher).launch {
            favoriteShowsUiState.update { FavoriteShowsUiState.Loading }
            try {
                val result =
                    getListOfFavoriteShowsUseCase(null).getOrThrow().sortedBy { it.name?.first() }
                favoriteShowsUiState.update { FavoriteShowsUiState.Success(result) }
            } catch (e: Exception) {
                favoriteShowsUiState.update {
                    FavoriteShowsUiState.Failure(
                        e.localizedMessage ?: ""
                    )
                }
            }
        }
    }

    fun deleteShowFromFavorite(show: ShowEntity) = CoroutineScope(coroutineDispatcher).launch {
        try {
            deleteFavoriteShowUseCase(show).getOrThrow()
            retrieveFavoriteShows()
        } catch (e: Exception) {
            favoriteShowsUiState.update { FavoriteShowsUiState.Failure(e.localizedMessage ?: "") }
        }
    }

}

sealed class FavoriteShowsUiState {
    object Loading : FavoriteShowsUiState()
    class Success(val shows: List<ShowEntity>) : FavoriteShowsUiState()
    class Failure(val error: String) : FavoriteShowsUiState()
}
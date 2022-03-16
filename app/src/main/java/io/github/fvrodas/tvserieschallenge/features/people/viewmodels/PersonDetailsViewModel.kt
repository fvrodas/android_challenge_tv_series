package io.github.fvrodas.tvserieschallenge.features.people.viewmodels

import androidx.lifecycle.ViewModel
import io.github.fvrodas.core.domain.entities.CrewCreditEntity
import io.github.fvrodas.core.domain.usecases.GetCastCreditsByIdUseCase
import io.github.fvrodas.core.domain.usecases.GetCrewCreditsByIdUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PersonDetailsViewModel(
    private val getCastCreditsByIdUseCase: GetCastCreditsByIdUseCase,
    private val getCrewCreditsByIdUseCase: GetCrewCreditsByIdUseCase,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    val personDetailsUiState: MutableStateFlow<PersonDetailsUiState> =
        MutableStateFlow(PersonDetailsUiState.Loading)

    fun retrievePersonDetailsById(personId: Long) {
        CoroutineScope(coroutineDispatcher).launch {
            personDetailsUiState.update { PersonDetailsUiState.Loading }
            try {
                val result = ArrayList<CrewCreditEntity>()
                result.addAll(getCastCreditsByIdUseCase(personId).getOrThrow())
                result.addAll(getCrewCreditsByIdUseCase(personId).getOrThrow().sortedBy { it.type })
                personDetailsUiState.update { PersonDetailsUiState.Success(result) }
            } catch (e: Exception) {
                personDetailsUiState.update { PersonDetailsUiState.Message(e.localizedMessage ?: "") }
            }
        }
    }
}

sealed class PersonDetailsUiState {
    object Loading : PersonDetailsUiState()
    class Success(val crewCredits: List<CrewCreditEntity>) : PersonDetailsUiState()
    class Message(val message: String) : PersonDetailsUiState()
}
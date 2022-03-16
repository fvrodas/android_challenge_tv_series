package io.github.fvrodas.tvserieschallenge.features.security.viewmodels

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PasswordViewModel(
    application: Application,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private var masterKey = MasterKey.Builder(application, MASTER_KEY_ALIAS)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()

    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        application,
        SHARED_PREFS_FILE,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    val passwordUiState: MutableStateFlow<PasswordUiState> =
        MutableStateFlow(PasswordUiState.Loading)

    fun createOrUpdatePassword(password: String) {
        CoroutineScope(coroutineDispatcher).launch {
            passwordUiState.update { PasswordUiState.Loading }
            try {
                sharedPreferences.edit().apply {
                    putString(PASSWORD_KEY, password)
                    apply()
                }
                passwordUiState.update { PasswordUiState.Created }
            } catch (e: Exception) {
                passwordUiState.update { PasswordUiState.Failure(e.localizedMessage ?: "") }
            }
        }
    }

    fun comparePassword(password: String) {
        CoroutineScope(coroutineDispatcher).launch {
            passwordUiState.update { PasswordUiState.Loading }
            try {
                val result = sharedPreferences.getString(PASSWORD_KEY, "")
                Log.d(PasswordViewModel::class.java.canonicalName, "$result")
                Log.d(PasswordViewModel::class.java.canonicalName, password)
                passwordUiState.update { PasswordUiState.Verified(result == password) }
            } catch (e: Exception) {
                passwordUiState.update { PasswordUiState.Failure(e.localizedMessage ?: "") }
            }
        }
    }

}

private const val SHARED_PREFS_FILE: String = "encrypted_password"
private const val MASTER_KEY_ALIAS: String = "tv_challenge_master_key"
private const val PASSWORD_KEY: String = "password_key"

sealed class PasswordUiState {
    object Loading : PasswordUiState()
    object Created : PasswordUiState()
    class Verified(val isCorrectPassword: Boolean) : PasswordUiState()
    class Failure(val error: String) : PasswordUiState()
}
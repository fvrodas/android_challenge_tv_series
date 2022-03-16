package io.github.fvrodas.tvserieschallenge.features.security.activities


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import io.github.fvrodas.tvserieschallenge.R
import io.github.fvrodas.tvserieschallenge.databinding.ActivityPasswordBinding
import io.github.fvrodas.tvserieschallenge.features.security.viewmodels.PasswordUiState
import io.github.fvrodas.tvserieschallenge.features.security.viewmodels.PasswordViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.Executor

class PasswordActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityPasswordBinding
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private var mode: Int = MODE_PASSWORD

    private val viewModel: PasswordViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityPasswordBinding.inflate(layoutInflater, null, false)
        setContentView(viewBinding.root)

        setSupportActionBar(viewBinding.passwordToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.password_title)

        mode = intent.getIntExtra(EXTRA_MODE, MODE_PASSWORD)

        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    viewBinding.passwordTextInputEdittext.requestFocus()
                    displayErrorMessage(getString(R.string.biometric_auth_error, errString))
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    setResult(RESULT_OK)
                    finish()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    displayErrorMessage(getString(R.string.biometric_auth_failed))
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.app_name))
            .setSubtitle(getString(R.string.biometric_subtitle))
            .setNegativeButtonText(getString(R.string.alternative_auth))
            .build()

        if (mode == MODE_PASSWORD && checkBiometrics()) {
            biometricPrompt.authenticate(promptInfo)
        } else {
            viewBinding.passwordTextInputEdittext.requestFocus()
        }

        viewBinding.passwordTextInputEdittext.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (viewBinding.passwordTextInputEdittext.text.isNullOrEmpty()
                    || viewBinding.passwordTextInputEdittext.text?.length ?: 0 < 6
                ) {
                    viewBinding.passwordTextInputLayout.error = getString(R.string.invalid_code)

                } else {
                    when (mode) {
                        MODE_CREATION -> {
                            viewModel.createOrUpdatePassword(viewBinding.passwordTextInputEdittext.text.toString())
                        }
                        MODE_PASSWORD -> {
                            viewModel.comparePassword(viewBinding.passwordTextInputEdittext.text.toString())
                        }
                    }
                }
            }
            true
        }

        lifecycleScope.launch {
            viewModel.passwordUiState.collect {
                when (it) {
                    is PasswordUiState.Loading -> {}
                    is PasswordUiState.Created -> {
                        setResult(RESULT_OK)
                        finish()
                    }
                    is PasswordUiState.Verified -> {
                        if (it.isCorrectPassword) {
                            setResult(RESULT_OK)
                            finish()
                        } else {
                            viewBinding.passwordTextInputLayout.error = getString(R.string.incorrect_code)
                        }
                    }
                    is PasswordUiState.Failure -> {
                        displayErrorMessage(it.error)
                    }
                }
            }
        }
    }

    private fun checkBiometrics(): Boolean {
        val biometricManager = BiometricManager.from(this)
        return when (biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                displayErrorMessage(getString(R.string.biometric_needs_enrollment))
                false
            }
            else -> false
        }
    }

    fun displayErrorMessage(message: String) {
        Toast.makeText(
            this@PasswordActivity,
            message,
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (mode == MODE_PASSWORD) {
                setResult(RESULT_CANCELED)
                finish()
            } else {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

const val EXTRA_MODE: String = "extra_mode"
const val MODE_CREATION: Int = 1
const val MODE_PASSWORD: Int = 2
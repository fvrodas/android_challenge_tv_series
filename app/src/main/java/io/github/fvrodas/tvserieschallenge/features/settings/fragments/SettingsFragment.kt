package io.github.fvrodas.tvserieschallenge.features.settings.fragments

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import io.github.fvrodas.tvserieschallenge.R
import io.github.fvrodas.tvserieschallenge.features.security.activities.EXTRA_MODE
import io.github.fvrodas.tvserieschallenge.features.security.activities.MODE_CREATION
import io.github.fvrodas.tvserieschallenge.features.security.activities.PasswordActivity

class SettingsFragment : PreferenceFragmentCompat() {

    private var passwordSwitch: SwitchPreference? = null

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != AppCompatActivity.RESULT_OK) {
                passwordSwitch?.sharedPreferences?.edit {
                    putBoolean("protected_by_password", false)
                    apply()
                }
                passwordSwitch?.isChecked = false
            }
        }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        passwordSwitch = findPreference<SwitchPreference>("protected_by_password")
        passwordSwitch?.setOnPreferenceChangeListener { _, newValue ->
            if (newValue as Boolean) {
                resultLauncher.launch(Intent(requireContext(), PasswordActivity::class.java).apply {
                    putExtra(EXTRA_MODE, MODE_CREATION)
                })
            }
            true
        }
    }

    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }
}
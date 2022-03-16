package io.github.fvrodas.tvserieschallenge.features.common.activities

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import io.github.fvrodas.tvserieschallenge.R
import io.github.fvrodas.tvserieschallenge.databinding.ActivityMainBinding
import io.github.fvrodas.tvserieschallenge.features.favorite_shows.fragments.FavoriteShowsFragment
import io.github.fvrodas.tvserieschallenge.features.people.fragments.PeopleFragment
import io.github.fvrodas.tvserieschallenge.features.security.activities.PasswordActivity
import io.github.fvrodas.tvserieschallenge.features.settings.fragments.SettingsFragment
import io.github.fvrodas.tvserieschallenge.features.shows.fragments.ShowsFragment


class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding

    private lateinit var prefs: SharedPreferences

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != RESULT_OK) {
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater, null, false)

        prefs = PreferenceManager.getDefaultSharedPreferences(this)

        viewBinding.mainBottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_menu_shows -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(viewBinding.fragmentContainerView.id, ShowsFragment.newInstance())
                        commit()
                    }
                }
                R.id.action_menu_people -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(
                            viewBinding.fragmentContainerView.id,
                            PeopleFragment.newInstance()
                        )
                        commit()
                    }
                }
                R.id.action_menu_favorite -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(
                            viewBinding.fragmentContainerView.id,
                            FavoriteShowsFragment.newInstance()
                        )
                        commit()
                    }
                }
                R.id.action_menu_settings -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(
                            viewBinding.fragmentContainerView.id,
                            SettingsFragment.newInstance()
                        )
                        commit()
                    }
                }
            }
            true
        }

        viewBinding.mainBottomNavigationView.selectedItemId = R.id.action_menu_shows

        setContentView(viewBinding.root)

        if (prefs.getBoolean("protected_by_password", false)) {
            resultLauncher.launch(Intent(this, PasswordActivity::class.java))
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        viewBinding.mainBottomNavigationView.selectedItemId = R.id.action_menu_shows
    }
}
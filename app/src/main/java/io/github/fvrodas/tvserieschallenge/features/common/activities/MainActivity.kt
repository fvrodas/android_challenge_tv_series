package io.github.fvrodas.tvserieschallenge.features.common.activities

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import io.github.fvrodas.tvserieschallenge.R
import io.github.fvrodas.tvserieschallenge.databinding.ActivityMainBinding
import io.github.fvrodas.tvserieschallenge.features.favorite_shows.fragments.FavoriteShowsFragment
import io.github.fvrodas.tvserieschallenge.features.people.fragments.PeopleFragment
import io.github.fvrodas.tvserieschallenge.features.shows.fragments.ShowsFragment

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater, null, false)

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
            }
            true
        }

        viewBinding.mainBottomNavigationView.selectedItemId = R.id.action_menu_shows

        setContentView(viewBinding.root)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        viewBinding.mainBottomNavigationView.selectedItemId = R.id.action_menu_shows
    }
}
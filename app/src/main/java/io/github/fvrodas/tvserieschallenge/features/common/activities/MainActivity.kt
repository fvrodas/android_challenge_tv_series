package io.github.fvrodas.tvserieschallenge.features.common.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import io.github.fvrodas.tvserieschallenge.R
import io.github.fvrodas.tvserieschallenge.databinding.ActivityMainBinding
import io.github.fvrodas.tvserieschallenge.features.shows.fragments.ShowsFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityMainBinding.inflate(layoutInflater, null, false)

        viewBinding.mainBottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.action_menu_shows -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(viewBinding.fragmentContainerView.id, ShowsFragment.newInstance())
                        commit()
                    }
                }
            }
            true
        }

        viewBinding.mainBottomNavigationView.selectedItemId = R.id.action_menu_shows

        setContentView(viewBinding.root)
    }
}
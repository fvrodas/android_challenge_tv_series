package io.github.fvrodas.tvserieschallenge

import android.app.Application
import io.github.fvrodas.core.coreModule
import io.github.fvrodas.core.BuildConfig
import io.github.fvrodas.tvserieschallenge.features.shows.viewmodels.ShowsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class TvSeriesChallengeApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@TvSeriesChallengeApplication)
            modules(appModule, coreModule)
        }
    }
}

val appModule = module {
    viewModel { ShowsViewModel(get(), get()) }
}
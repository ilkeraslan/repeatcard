package com.repeatcard.app

import androidx.multidex.MultiDexApplication
import com.jakewharton.threetenabp.AndroidThreeTen
import com.repeatcard.app.di.androidComponents
import com.repeatcard.app.di.appComponents
import com.repeatcard.app.di.viewModels
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

private const val TAG_LOGGING = "REPEATCARD"

class RepeatcardApplication : MultiDexApplication() {

    @ExperimentalCoroutinesApi
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        setupLogging()
        setupDI()
    }

    private fun setupLogging() {
        Timber.plant(Timber.DebugTree())
        Timber.tag(TAG_LOGGING)
    }

    @ExperimentalCoroutinesApi
    private fun setupDI() {
        startKoin {
            androidLogger()
            androidContext(this@RepeatcardApplication)

            val appSetupModule = module { single { BuildConfig.DEBUG } }

            modules(
                listOf(
                    appSetupModule,
                    androidComponents,
                    appComponents,
                    viewModels
                )
            )
        }
    }
}

package it.ilker.repeatcard

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.multidex.MultiDexApplication
import com.jakewharton.threetenabp.AndroidThreeTen
import it.ilker.repeatcard.di.androidComponents
import it.ilker.repeatcard.di.appComponents
import it.ilker.repeatcard.di.viewModels
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module
import timber.log.Timber

private const val TAG_LOGGING = "REPEATCARD"

@ExperimentalUnitApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
class RepeatcardApplication : MultiDexApplication() {

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

    @ExperimentalMaterialApi
    @ExperimentalCoroutinesApi
    private fun setupDI() {
        startKoin {
            // Workaround, with Koin 3.2.0 will be resolved
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
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

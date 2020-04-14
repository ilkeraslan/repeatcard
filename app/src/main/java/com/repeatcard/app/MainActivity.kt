package com.repeatcard.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
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

class MainActivity : AppCompatActivity() {

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val host: NavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = host.navController

        navView.setupWithNavController(navController)
        navView.setOnNavigationItemReselectedListener { /* do nothing */ }

        // Initialize date library
        AndroidThreeTen.init(this)

        setupLogging()
        setupDI()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun setupLogging() {
        Timber.plant(Timber.DebugTree())
        Timber.tag(TAG_LOGGING)
    }

    @ExperimentalCoroutinesApi
    private fun setupDI() {
        startKoin {
            androidLogger()
            androidContext(this@MainActivity)

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

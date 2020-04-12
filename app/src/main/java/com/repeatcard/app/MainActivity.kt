package com.repeatcard.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jakewharton.threetenabp.AndroidThreeTen
import timber.log.Timber

private const val TAG_LOGGING = "REPEATCARD"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val host: NavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = host.navController

        /* val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_home, R.id.navigation_quiz, R.id.navigation_directories, R.id.navigation_notifications)
        ) */

        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setOnNavigationItemReselectedListener { /* do nothing */ }

        // Initialize date library
        AndroidThreeTen.init(this)

        setupLogging()
    }

    private fun setupLogging() {
        Timber.plant(Timber.DebugTree())
        Timber.tag(TAG_LOGGING)
    }
}

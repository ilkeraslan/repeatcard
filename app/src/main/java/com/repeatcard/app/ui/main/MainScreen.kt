package com.repeatcard.app.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.repeatcard.app.R
import kotlinx.coroutines.ExperimentalCoroutinesApi

class MainScreen : AppCompatActivity() {

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val host: NavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = host.navController

        navView.setupWithNavController(navController)
        navView.setOnNavigationItemReselectedListener { /* do nothing */ }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}

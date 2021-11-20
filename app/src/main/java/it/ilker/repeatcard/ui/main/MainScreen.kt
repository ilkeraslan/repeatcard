package it.ilker.repeatcard.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.navigationBarsPadding
import it.ilker.repeatcard.bottomNavItems
import it.ilker.repeatcard.navigation.AppBottomNavigation
import it.ilker.repeatcard.navigation.NavHostFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
class MainScreen : AppCompatActivity() {

    companion object {
        fun openScreen(context: Context) {
            val intent = Intent(context, MainScreen::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Host()
        }
    }

    @ExperimentalCoroutinesApi
    @Composable
    private fun Host() {
        val navController = rememberNavController()
        val coroutineScope = rememberCoroutineScope()
        val scrollState = rememberScrollState()

        val bottomBar: @Composable () -> Unit = {
            AppBottomNavigation(
                navController = navController,
                items = bottomNavItems
            )
        }

        Scaffold(
            modifier = Modifier
                .navigationBarsPadding()
                .fillMaxSize(),
            bottomBar = bottomBar
        ) {
            NavHostFactory(navController)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}

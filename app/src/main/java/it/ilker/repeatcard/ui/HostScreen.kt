package it.ilker.repeatcard.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import it.ilker.repeatcard.ui.flashcarddetail.FlashcardDetailFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class HostScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Host()
        }
    }
}

@ExperimentalCoroutinesApi
@Composable
private fun Host() {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Scaffold() {
        NavHost(
            navController = navController,
            startDestination = "placeholder"
        ) {
            FlashcardDetailFactory.create(
                navGraphBuilder = this,
                navController = navController
            )
        }
    }
}

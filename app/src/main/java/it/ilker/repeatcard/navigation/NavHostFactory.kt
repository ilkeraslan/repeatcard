package it.ilker.repeatcard.navigation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import it.ilker.repeatcard.ui.addcard.AddCardFactory
import it.ilker.repeatcard.ui.directories.DirectoriesFactory
import it.ilker.repeatcard.ui.flashcarddetail.FlashcardDetailFactory
import it.ilker.repeatcard.ui.home.HomeFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
@Composable
internal fun NavHostFactory(navController: NavHostController) = NavHost(
    navController = navController,
    startDestination = Screen.HomeScreen.route
) {
    composable(Screen.HomeScreen.route) {
        HomeFactory.Create(
            navGraphBuilder = this,
            navController = navController
        )
    }

    composable(Screen.DirectoriesScreen.route) {
        DirectoriesFactory.Create(
            navGraphBuilder = this,
            navController = navController
        )
    }

    composable(Screen.FlashcardDetailScreen.route) {
        FlashcardDetailFactory.Create(
            navGraphBuilder = this,
            navController = navController
        )
    }

    composable(Screen.AddCardScreen.route) {
        AddCardFactory.Create(
            navGraphBuilder = this,
            navController = navController
        )
    }
}

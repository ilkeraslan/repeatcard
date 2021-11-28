package it.ilker.repeatcard.navigation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
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
    HomeFactory.create(
        navGraphBuilder = this,
        navController = navController
    )

    DirectoriesFactory.create(
        navGraphBuilder = this,
        navController = navController
    )

    FlashcardDetailFactory.create(
        navGraphBuilder = this,
        navController = navController
    )
}

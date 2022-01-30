package it.ilker.repeatcard.navigation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import it.ilker.repeatcard.ui.addcard.AddCardFactory
import it.ilker.repeatcard.ui.directories.DirectoriesFactory
import it.ilker.repeatcard.ui.flashcarddetail.FlashcardDetailFactory
import it.ilker.repeatcard.ui.home.HomeFactory
import it.ilker.repeatcard.ui.quiz.QuizFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalUnitApi
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

    composable(Screen.Quiz.route) {
        QuizFactory.Create(
            navGraphBuilder = this,
            navController = navController
        )
    }
}

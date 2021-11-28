package it.ilker.repeatcard.ui.flashcarddetail

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import it.ilker.repeatcard.navigation.NavFactory
import it.ilker.repeatcard.navigation.Screen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.viewModel

@ExperimentalCoroutinesApi
object FlashcardDetailFactory : NavFactory {
    override fun create(navGraphBuilder: NavGraphBuilder, navController: NavController) {
        navGraphBuilder.composable(Screen.FlashcardDetailScreen.route) {
            val vm by viewModel<FlashcardDetailViewModel>()
            val state by vm.state.collectAsState()

            FlashcardDetail()
        }
    }
}

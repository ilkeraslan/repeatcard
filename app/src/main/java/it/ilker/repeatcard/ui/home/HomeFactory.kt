package it.ilker.repeatcard.ui.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import it.ilker.repeatcard.navigation.NavFactory
import it.ilker.repeatcard.navigation.Screen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.viewModel

@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
object HomeFactory : NavFactory {
    override fun create(navGraphBuilder: NavGraphBuilder, navController: NavController) {
        navGraphBuilder.composable(Screen.HomeScreen.route) {
            val vm by viewModel<HomeViewModel>()
            val state = vm.state.collectAsState()

            when (val value = state.value) {
                FlashcardState.Error -> { /* no-op */ }
                FlashcardState.Loading -> { /* no-op */ }
                is FlashcardState.Success -> HomeScreen(
                    modifier = Modifier.fillMaxWidth(),
                    flashcards = value.flashcards
                )
            }
        }
    }
}

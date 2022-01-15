package it.ilker.repeatcard.ui.flashcarddetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import it.ilker.repeatcard.navigation.NavFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.viewModel

@ExperimentalCoroutinesApi
object FlashcardDetailFactory : NavFactory {
    @Composable
    override fun Create(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController
    ) {
        val vm by viewModel<FlashcardDetailViewModel>()
        val state by vm.state.collectAsState()

        FlashcardDetail()
    }
}

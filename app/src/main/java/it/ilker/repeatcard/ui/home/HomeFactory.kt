package it.ilker.repeatcard.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.insets.navigationBarsPadding
import it.ilker.repeatcard.navigation.NavFactory
import it.ilker.repeatcard.navigation.Screen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.ilker.business.flashcard.Flashcard
import org.koin.androidx.compose.viewModel

@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
object HomeFactory : NavFactory {
    @Composable
    override fun Create(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController
    ) {
        val vm by viewModel<HomeViewModel>()
        val state = vm.state.collectAsState()

        Home(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(bottom = 60.dp),
//                    flashcards = value.flashcards
            flashcards = listOf(
                Flashcard(
                    id = 5,
                    title = "foo"
                )
            ),
            onAddCard = {
                navController.navigate(Screen.AddCardScreen.route)
            }
        )

        when (val value = state.value) {
            FlashcardState.Error -> { /* no-op */
            }
            FlashcardState.Loading -> { /* no-op */
            }
            is FlashcardState.Success -> Home(
                modifier = Modifier.fillMaxSize(),
//                    flashcards = value.flashcards
                flashcards = listOf(
                    Flashcard(
                        id = 5,
                        title = "foo"
                    )
                )
            )
        }
    }
}

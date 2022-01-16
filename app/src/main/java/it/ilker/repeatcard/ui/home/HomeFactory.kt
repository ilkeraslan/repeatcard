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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.ilker.design.Error
import me.ilker.design.Loading
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

        when (val value = state.value) {
            FlashcardState.Error -> Error(
                modifier = Modifier.fillMaxSize()
            )
            FlashcardState.Loading -> Loading(
                modifier = Modifier.fillMaxSize()
            )
            is FlashcardState.Success -> Home(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .padding(bottom = 60.dp),
                flashcards = value.flashcards
            )
        }
    }
}

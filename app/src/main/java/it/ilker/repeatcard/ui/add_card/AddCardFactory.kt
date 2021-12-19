package it.ilker.repeatcard.ui.add_card

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
object AddCardFactory : NavFactory {
    override fun create(navGraphBuilder: NavGraphBuilder, navController: NavController) {
        navGraphBuilder.composable(Screen.HomeScreen.route) {
            val vm by viewModel<AddCardViewModel>()
            val state = vm.state.collectAsState()

            when (val value = state.value) {
                AddCardState.Initial -> AddCard(Modifier.fillMaxWidth()) { title ->
                    vm.send(AddCardEvent.Add(title))
                }
                AddCardState.Error -> { /* no-op */ }
                AddCardState.Loading -> { /* no-op */ }
                is AddCardState.Success -> { navController.popBackStack() }
            }
        }
    }
}

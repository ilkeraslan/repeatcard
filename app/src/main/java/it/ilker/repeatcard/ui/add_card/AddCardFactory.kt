package it.ilker.repeatcard.ui.add_card

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import it.ilker.repeatcard.navigation.NavFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.viewModel

@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
object AddCardFactory : NavFactory {
    @Composable
    override fun Create(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController
    ) {
        val vm by viewModel<AddCardViewModel>()
        val state = vm.state.collectAsState()

        when (val value = state.value) {
            AddCardState.Initial -> AddCard(Modifier.fillMaxWidth()) { title ->
                vm.send(AddCardEvent.Add(title))
            }
            AddCardState.Error -> { /* no-op */ }
            AddCardState.Loading -> { /* no-op */ }
            is AddCardState.Success -> {
                navController.popBackStack()
            }
        }
    }
}

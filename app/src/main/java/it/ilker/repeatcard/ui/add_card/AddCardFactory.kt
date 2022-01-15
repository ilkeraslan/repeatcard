package it.ilker.repeatcard.ui.add_card

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import it.ilker.repeatcard.navigation.NavFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.ilker.design.Error
import me.ilker.design.Loading
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
            AddCardState.Initial -> AddCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(25.dp),
                onAdded = { title ->
                    vm.send(AddCardEvent.Add(title))
                },
                onBackPressed = { navController.popBackStack() }
            )
            AddCardState.Error -> Error(
                modifier = Modifier.fillMaxSize()
            )
            AddCardState.Loading -> Loading(
                modifier = Modifier.fillMaxSize()
            )
            is AddCardState.Success -> navController.popBackStack()
        }
    }
}

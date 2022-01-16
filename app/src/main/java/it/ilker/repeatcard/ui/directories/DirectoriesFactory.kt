package it.ilker.repeatcard.ui.directories

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import it.ilker.repeatcard.navigation.NavFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.ilker.design.Error
import me.ilker.design.Loading
import org.koin.androidx.compose.viewModel

@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
object DirectoriesFactory : NavFactory {
    @Composable
    override fun Create(navGraphBuilder: NavGraphBuilder, navController: NavController) {
        val vm by viewModel<DirectoriesViewModel>()
        val directories = vm.directoriesState.collectAsState()

        when (val state = directories.value) {
            is DirectoriesState.Error -> Error(
                modifier = Modifier.fillMaxSize()
            )
            DirectoriesState.Loading -> Loading(
                modifier = Modifier.fillMaxSize()
            )
            is DirectoriesState.Success -> Directories(
                modifier = Modifier.fillMaxSize(),
                directories = state.directories
            )
        }
    }
}

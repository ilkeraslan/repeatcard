package it.ilker.repeatcard.ui.directories

import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import it.ilker.repeatcard.navigation.NavFactory
import it.ilker.repeatcard.navigation.Screen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.viewModel
import timber.log.Timber

@ExperimentalCoroutinesApi
object DirectoriesFactory : NavFactory {
    override fun create(navGraphBuilder: NavGraphBuilder, navController: NavController) {
        navGraphBuilder.composable(Screen.DirectoriesScreen.route) {
            val vm by viewModel<DirectoriesViewModel>()

            val directories = vm.allDirectories.collectAsState()
            Timber.d(directories.value.toString())

            DirectoriesScreen()
        }
    }
}

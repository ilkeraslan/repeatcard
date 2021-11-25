package it.ilker.repeatcard.ui.directories

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import it.ilker.repeatcard.NavFactory
import it.ilker.repeatcard.Screen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.viewModel

@ExperimentalCoroutinesApi
object DirectoriesFactory : NavFactory {
    override fun create(navGraphBuilder: NavGraphBuilder, navController: NavController) {
        navGraphBuilder.composable(Screen.DirectoriesScreen.route) {
            val vm by viewModel<DirectoriesViewModel>()

            DirectoriesScreen()
        }
    }
}

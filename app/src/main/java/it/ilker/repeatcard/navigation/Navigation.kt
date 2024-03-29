package it.ilker.repeatcard.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

interface NavFactory {
    @Composable
    fun Create(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController
    )
}

sealed class Screen(
    val route: String,
    val label: String,
    val icon: ImageVector? = null
) {
    object HomeScreen : Screen(route = "home", label = "Home", icon = Icons.Rounded.Home)
    object DirectoriesScreen : Screen(route = "directories", label = "Directories", icon = Icons.Rounded.List)
    object FlashcardDetailScreen : Screen(route = "flashcard_detail", label = "Flashcard Detail")
    object AddCardScreen : Screen(route = "add_card", label = "Add Flashcard")
    object Quiz : Screen(route = "quiz", label = "Quiz", icon = Icons.Rounded.Done)
}

data class BottomNavItem(val screen: Screen)

val bottomNavItems = listOf(
    BottomNavItem(Screen.HomeScreen),
    BottomNavItem(Screen.DirectoriesScreen),
    BottomNavItem(Screen.Quiz)
)

package it.ilker.repeatcard.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

interface NavFactory {
    fun create(navGraphBuilder: NavGraphBuilder, navController: NavController)
}

sealed class Screen(
    val route: String,
    val label: String,
    val icon: ImageVector? = null
) {
    object HomeScreen : Screen(route = "home", label = "Home")
    object DirectoriesScreen : Screen(route = "directories", label = "Directories")
    object FlashcardDetailScreen : Screen(route = "flashcard_detail", label = "Flashcard Detail")
    object AddCardScreen : Screen(route = "add_card", label = "Add Flashcard")
}

data class BottomNavItem(val screen: Screen)

val bottomNavItems = listOf(
    BottomNavItem(Screen.HomeScreen),
    BottomNavItem(Screen.DirectoriesScreen)
)

package it.ilker.repeatcard.navigation

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import it.ilker.repeatcard.BottomNavItem

@Composable
internal fun AppBottomNavigation(
    navController: NavHostController,
    items: List<BottomNavItem>
) {
    BottomNavigation(
        backgroundColor = LightGray,
        contentColor = White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.map { it.screen }
            .forEach { screen ->
                BottomNavigationItem(
                    icon = {
                        screen.icon?.let {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.label
                            )
                        }
                    },
                    label = { Text(screen.label) },
                    selected = currentRoute == screen.route,
                    onClick = {
                        if (currentRoute != screen.route) {
                            navController.navigate(screen.route)
                        }
                    }
                )
            }
    }
}

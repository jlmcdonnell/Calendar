package dev.mcd.calendar.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.mcd.calendar.ui.home.HomeScreen

@Composable
fun Routing(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "/home",
    ) {
        composable("/home") {
            HomeScreen()
        }
    }
}

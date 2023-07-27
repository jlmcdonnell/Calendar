package dev.mcd.calendar.ui.routing

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dev.mcd.calendar.ui.calendar.CalendarScreen
import dev.mcd.calendar.ui.events.create.CreateEventScreen

@Composable
fun Routing(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "/calendar",
    ) {
        composable("/calendar") {
            CalendarScreen { date ->
                navController.navigate("/events/create?date=${date.navArg()}")
            }
        }
        composable(
            route = "/events/create?date={date}",
            arguments = listOf(navArgument(name = "date") {}),
        ) {
            CreateEventScreen()
        }
    }
}
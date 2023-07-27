package dev.mcd.calendar.ui.routing

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dev.mcd.calendar.ui.calendar.month.CalendarMonthScreen
import dev.mcd.calendar.ui.events.create.CreateEventScreen

@Composable
fun Routing(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "/calendar",
    ) {
        composable("/calendar") {
            CalendarMonthScreen(
                onNavigateCreateEvent = { date ->
                    navController.navigate("/events/create?date=${date.navArg()}")
                },
                onNavigateDay = { date ->
                    navController.navigate("/calendar/day?date=${date.navArg()}")
                },
            )
        }
        composable(
            route = "/calendar/day?date={date}",
            arguments = listOf(navArgument(name = "date") {}),
        ) {
        }
        composable(
            route = "/events/create?date={date}",
            arguments = listOf(navArgument(name = "date") {}),
        ) {
            CreateEventScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
            )
        }
    }
}

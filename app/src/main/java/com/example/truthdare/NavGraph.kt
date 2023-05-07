package com.example.truthdare

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    truths: MutableList<String>,
    dares: MutableList<String>,
    repository: TruthOrDareRepository
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(
            route = Screen.Home.route
        ) {
            HomeScreen(navController = navController, truths = truths, dares = dares)
        }
        composable(
            route = Screen.Add.route
        ) {
            AddScreen(navController = navController, truths = truths, dares = dares, repository = repository)
        }
    }
}

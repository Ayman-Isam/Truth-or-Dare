package com.example.truthdare

import ViewScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    truths: MutableList<String>,
    dares: MutableList<String>,
    initialTruths: List<TruthOrDare>,
    initialDares: List<TruthOrDare>,
    truthsState: MutableState<List<TruthOrDare>>,
    daresState: MutableState<List<TruthOrDare>>,
    repository: TruthOrDareRepository,
    lifecycleScope: LifecycleCoroutineScope,
) {

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(
            route = Screen.Home.route
        ) {
            HomeScreen(navController = navController, repository = repository)
        }
        composable(
            route = Screen.Add.route
        ) {
            AddScreen(navController = navController, truths = truths, dares = dares, repository = repository, lifecycleScope = lifecycleScope)
        }
        composable(
            route = Screen.View.route
        ) {
            val truthsState = remember { mutableStateOf(emptyList<TruthOrDare>()) }
            val daresState = remember { mutableStateOf(emptyList<TruthOrDare>()) }

            LaunchedEffect(Unit) {
                truthsState.value = repository.getTruths()
                daresState.value = repository.getDares()
            }

            ViewScreen(
                navController = navController,
                initialTruths = truthsState.value,
                initialDares = daresState.value,
                repository = repository
            )
        }

    }
}

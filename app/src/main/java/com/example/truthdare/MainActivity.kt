package com.example.truthdare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.truthdare.ui.theme.TruthDareTheme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private lateinit var repository: TruthOrDareRepository
    lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = TruthOrDareRepository(this)

        lifecycleScope.launch {
            val truths = repository.getTruths().map { it.text }.toMutableList()
            val dares = repository.getDares().map { it.text }.toMutableList()

            setContent {
                TruthDareTheme {
                    navController = rememberNavController()
                    SetupNavGraph(navController = navController, truths = truths, dares = dares, repository = repository, lifecycleScope = lifecycleScope)
                }
            }
        }
    }
}










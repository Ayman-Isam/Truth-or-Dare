package com.example.truthdare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.truthdare.ui.theme.TruthDareTheme


class MainActivity : ComponentActivity() {
    private val repository = TruthOrDareRepository(this)
    lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val truths = repository.getTruths().map { it.text }.toMutableList()
        val dares = repository.getDares().map { it.text }.toMutableList()

        setContent {
            TruthDareTheme {
                navController = rememberNavController()
                SetupNavGraph(navController = navController, truths = truths, dares = dares, repository = repository)
            }
        }
    }
}








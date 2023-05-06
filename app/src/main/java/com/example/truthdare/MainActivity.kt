package com.example.truthdare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.truthdare.ui.theme.TruthDareTheme


class MainActivity : ComponentActivity() {

    lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TruthDareTheme {
                navController = rememberNavController()
                SetupNavGraph(navController = navController)
            }
        }
    }
}








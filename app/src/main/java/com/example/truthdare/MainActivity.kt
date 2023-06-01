package com.example.truthdare

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
        val repository = TruthOrDareRepository(this)

        lifecycleScope.launch {
            val truths = repository.getTruths().map { it.text }.toMutableList()
            val dares = repository.getDares().map { it.text }.toMutableList()

            setContent {
                val truthsState = remember { mutableStateOf(emptyList<TruthOrDare>()) }
                val daresState = remember { mutableStateOf(emptyList<TruthOrDare>()) }

                val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
                val isFirstLaunch = sharedPreferences.getBoolean("isFirstLaunch", true)
                if (isFirstLaunch) {
                    // Load truths from arrays.xml and insert them into the database
                    val truthsArray = resources.getStringArray(R.array.truths)
                    LaunchedEffect(Unit) {
                        val repository = TruthOrDareRepository(this@MainActivity)
                        // Load truths from arrays.xml and insert them into the database
                        val truthsArray = resources.getStringArray(R.array.truths)
                        for (truth in truthsArray) {
                            val newTruth = TruthOrDare(text = truth, isTruth = true)
                            repository.insert(newTruth)
                        }
                        // Load dares from arrays.xml and insert them into the database
                        val daresArray = resources.getStringArray(R.array.dares)
                        for (dare in daresArray) {
                            val newDare = TruthOrDare(text = dare, isTruth = false)
                            repository.insert(newDare)
                        }
                    }
                    // Set isFirstLaunch to false so that this code is not executed again
                    with(sharedPreferences.edit()) {
                        putBoolean("isFirstLaunch", false)
                        apply()
                    }
                }

                TruthDareTheme {
                    navController = rememberNavController()
                    SetupNavGraph(
                        navController = navController,
                        initialTruths = truthsState.value,
                        initialDares = daresState.value,
                        truthsState = truthsState,
                        daresState = daresState,
                        repository = repository,
                        truths = truths,
                        dares = dares,
                        lifecycleScope = lifecycleScope)
                }
            }
        }
    }
}










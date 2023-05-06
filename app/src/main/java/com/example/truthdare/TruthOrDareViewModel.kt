package com.example.truthdare

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class TruthOrDareViewModel(private val context: Context): ViewModel() {
    val truths = mutableStateListOf(*context.resources.getStringArray(R.array.truths))
    val dares = mutableStateListOf(*context.resources.getStringArray(R.array.dares))
}
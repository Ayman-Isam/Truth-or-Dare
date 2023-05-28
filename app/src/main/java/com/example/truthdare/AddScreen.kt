package com.example.truthdare

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavController
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    navController: NavController,
    truths: MutableList<String>,
    dares: MutableList<String>,
    repository: TruthOrDareRepository,
    lifecycleScope: LifecycleCoroutineScope
) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    var showError by remember { mutableStateOf(false) }

    val options = listOf("Truth", "Dare")
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }

    MaterialTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp)
                ), title = {
                    Text(
                        "Add Truths or Dares",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 24.sp
                    )
                }, navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(route = Screen.Home.route) {
                            popUpTo(Screen.Home.route) {
                                inclusive = true
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Localized description",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }, actions = {
                    IconButton(onClick = { navController.navigate(Screen.View.route) }) {
                        Icon(
                            imageVector = Icons.Filled.List,
                            contentDescription = "Localized description",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                })
            },
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                    ) {

                        OutlinedTextField(
                            modifier = Modifier
                                .width(360.dp)
                                .menuAnchor(),
                            readOnly = true,
                            value = selectedOptionText,
                            onValueChange = {},
                            label = { Text("Category") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        )
                        ExposedDropdownMenu(expanded = expanded,
                            onDismissRequest = { expanded = false }) {
                            options.forEach { selectedOption ->
                                DropdownMenuItem(text = { Text(selectedOption) },
                                    onClick = {
                                        selectedOptionText = selectedOption
                                        expanded = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                )
                            }
                        }
                    }
                    val spacerHeight by animateDpAsState(targetValue = if (expanded) 120.dp else 10.dp)

                    Spacer(modifier = Modifier.height(spacerHeight))
                    Spacer(modifier = Modifier.height(20.dp))
                    TextField(
                        value = text,
                        onValueChange = {
                            text = it
                            showError = false
                        },
                        label = { Text("Label") },
                        modifier = Modifier.width(360.dp)
                    )
                    if (showError) {
                        Text("Field cannot be empty", color = Color.Red)
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(onClick = {
                        if (text.text.isEmpty()) {
                            showError = true
                        } else {
                            lifecycleScope.launch {
                                if (selectedOptionText == "Truth") {
                                    repository.insert(TruthOrDare(text = text.text, isTruth = true))
                                } else if (selectedOptionText == "Dare") {
                                    repository.insert(TruthOrDare(text = text.text, isTruth = false))
                                }
                                text = TextFieldValue("")
                                navController.navigate(route = Screen.View.route) {
                                    popUpTo(Screen.View.route) {
                                        inclusive = true
                                    }
                                }
                            }
                        }
                    }) {
                        Text("Add")
                    }
                }
            },
        )
    }
}

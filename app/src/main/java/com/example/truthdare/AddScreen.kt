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
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    navController: NavController,
    truths: MutableList<String>,
    dares: MutableList<String>,
    repository: TruthOrDareRepository
) {
    var text by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("example", TextRange(0, 7)))
    }

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
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
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
                    val spacerHeight by animateDpAsState(targetValue = if (expanded) 120.dp else 0.dp)

                    Spacer(modifier = Modifier.height(spacerHeight))
                    Spacer(modifier = Modifier.height(20.dp))
                    TextField(value = text,
                        onValueChange = { text = it },
                        label = { Text("Label") },
                        modifier = Modifier.width(360.dp)
                    )
                    Button(onClick = {
                        if (selectedOptionText == "Truth") {
                            repository.insert(TruthOrDare(text = text.text, isTruth = true))
                        } else if (selectedOptionText == "Dare") {
                            repository.insert(TruthOrDare(text = text.text, isTruth = false))
                        }
                        text = TextFieldValue("")
                    }) {
                        Text("Add")
                    }

                }
            },
        )
    }
}

@Composable
@Preview(showBackground = true)
fun AddScreenPreview() {
    val context = LocalContext.current
    val repository = TruthOrDareRepository(context)
    val truths = mutableListOf("Truth 1", "Truth 2")
    val dares = mutableListOf("Dare 1", "Dare 2")
    AddScreen(
        navController = rememberNavController(),
        truths = truths,
        dares = dares,
        repository = repository
    )
}

/*
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
    val spacerHeight by animateDpAsState(targetValue = if (expanded) 120.dp else 0.dp)

    Spacer(modifier = Modifier.height(spacerHeight))
    Spacer(modifier = Modifier.height(20.dp))
    TextField(value = text,
        onValueChange = { text = it },
        label = { Text("Label") },
        modifier = Modifier.width(360.dp)
    )
    Button(onClick = { addText(text.text) }) {
        Text("Add")
    }
}
},
*/
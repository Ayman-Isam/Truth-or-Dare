
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.truthdare.Screen
import com.example.truthdare.TruthOrDare
import com.example.truthdare.TruthOrDareRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewScreen(
    navController: NavController,
    initialTruths: List<TruthOrDare>,
    initialDares: List<TruthOrDare>,
    repository: TruthOrDareRepository
) {
    var selectedTabIndex by remember { mutableStateOf(0) }

    // Use a MutableState object to hold the list of truths and dares
    val truths = remember { mutableStateOf(emptyList<TruthOrDare>()) }
    val dares = remember { mutableStateOf(emptyList<TruthOrDare>()) }

    LaunchedEffect(Unit) {
        truths.value = repository.getTruths()
        dares.value = repository.getDares()
    }

    MaterialTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp)
                    ),
                    title = {
                        Text(
                            "View Truths Or Dares",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 24.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.navigate(route = Screen.Add.route) {
                                popUpTo(Screen.Add.route) {
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
                    }
                )
            },
            content = { innerPadding ->
                Column(
                    modifier = Modifier.padding(innerPadding)
                ) {

                    // Get a reference to a CoroutineScope
                    val coroutineScope = rememberCoroutineScope()

                    // Update the list of truths or dares when an item is deleted
                    val onDelete: (TruthOrDare) -> Unit = { truthOrDare ->
                        // Launch a new coroutine using the CoroutineScope
                        coroutineScope.launch {
                            repository.delete(truthOrDare)
                            if (truthOrDare.isTruth) {
                                truths.value = repository.getTruths()
                            } else {
                                dares.value = repository.getDares()
                            }
                        }
                    }

                    when (selectedTabIndex) {
                        0 -> TruthsList(truths.value, repository, onDelete, truths, dares)
                        1 -> DaresList(dares.value, repository, onDelete, truths, dares)
                    }
                }
            },
            bottomBar = {
                Column {
                    TabRow(
                        selectedTabIndex = selectedTabIndex,
                        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp)
                    ) {
                        Tab(
                            text = { Text(text = "Truths")},
                            selected = selectedTabIndex == 0,
                            onClick = { selectedTabIndex = 0 },

                        )
                        Tab(
                            text = { Text(text = "Dares")},
                            selected = selectedTabIndex == 1,
                            onClick = { selectedTabIndex = 1 },
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun TruthsList(
    truths: List<TruthOrDare>,
    repository: TruthOrDareRepository,
    onDelete: (TruthOrDare) -> Unit,
    truthsState: MutableState<List<TruthOrDare>>,
    daresState: MutableState<List<TruthOrDare>>
){
    val coroutineScope = rememberCoroutineScope()
    var truthOrDareToEdit by remember { mutableStateOf<TruthOrDare?>(null) }
    var truthOrDareToDelete by remember { mutableStateOf<TruthOrDare?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (truthOrDareToEdit != null) {
        EditTruthOrDareDialog(
            truthOrDare = truthOrDareToEdit!!,
            onEdit = { truthOrDare ->
                coroutineScope.launch {
                    repository.update(truthOrDare)
                    truthOrDareToEdit = null
                    if (truthOrDare.isTruth) {
                        truthsState.value = repository.getTruths()
                    } else {
                        daresState.value = repository.getDares()
                    }
                }
            },
            onDismiss = { truthOrDareToEdit = null }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(text = "Delete Truth Or Dare") },
            text = { Text(text = "Are you sure you want to delete this truth or dare?") },
            confirmButton = {
                TextButton(onClick = {
                    coroutineScope.launch {
                        onDelete(truthOrDareToDelete!!)
                        showDeleteDialog = false
                    }
                }) {
                    Text(text = "Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(text = "No")
                }
            }
        )
    }

    LazyColumn() {
        items(truths.size) { index ->
            val reversedIndex = truths.lastIndex - index
            val item = truths[reversedIndex]

            Row(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = item.text,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1f)
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(onClick = {
                    truthOrDareToEdit = item
                }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit"
                    )
                }

                IconButton(onClick = {
                    truthOrDareToDelete = item
                    showDeleteDialog = true
                }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete"
                    )
                }
            }
            Divider(color = MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp))
        }
    }
}



@Composable
fun DaresList(
    dares: List<TruthOrDare>,
    repository: TruthOrDareRepository,
    onDelete: (TruthOrDare) -> Unit,
    truthsState: MutableState<List<TruthOrDare>>,
    daresState: MutableState<List<TruthOrDare>>
){
    val coroutineScope = rememberCoroutineScope()
    var truthOrDareToEdit by remember { mutableStateOf<TruthOrDare?>(null) }


    if (truthOrDareToEdit != null) {
        EditTruthOrDareDialog(
            truthOrDare = truthOrDareToEdit!!,
            onEdit = { truthOrDare ->
                coroutineScope.launch {
                    repository.update(truthOrDare)
                    truthOrDareToEdit = null
                    if (truthOrDare.isTruth) {
                        truthsState.value = repository.getTruths()
                    } else {
                        daresState.value = repository.getDares()
                    }
                }
            },
            onDismiss = { truthOrDareToEdit = null }

        )
    }

    LazyColumn() {
        items(dares.size) { index ->
            val reversedIndex = dares.lastIndex - index
            val item = dares[reversedIndex]

            Row(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = item.text,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1f)
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(onClick = {
                    truthOrDareToEdit = item
                }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit"
                    )
                }

                IconButton(onClick = {
                    // Launch a new coroutine using the CoroutineScope
                    coroutineScope.launch {
                        val truthOrDareToDelete = item
                        onDelete(truthOrDareToDelete)
                    }
                }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete"
                    )
                }
            }
            Divider(color = MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp))
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTruthOrDareDialog(
    truthOrDare: TruthOrDare,
    onEdit: (TruthOrDare) -> Unit,
    onDismiss: () -> Unit
) {
    var text by remember { mutableStateOf(truthOrDare.text) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Edit Truth Or Dare") },
        text = {
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text(text = "Text") }
            )
        },
        confirmButton = {
            TextButton(onClick = {
                onEdit(truthOrDare.copy(text = text))
                onDismiss()
            }) {
                Text(text = "Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        }
    )
}

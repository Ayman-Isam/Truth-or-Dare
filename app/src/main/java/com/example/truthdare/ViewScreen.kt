
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.truthdare.Screen
import com.example.truthdare.TruthOrDare

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewScreen(
    navController: NavController,
    truths: List<TruthOrDare>,
    dares: List<TruthOrDare>
) {
    var selectedTabIndex by remember { mutableStateOf(0) }

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
                    when (selectedTabIndex) {
                        0 -> TruthsList(truths)
                        1 -> DaresList(dares)
                    }
                }
            },
            bottomBar = {
                Column {
                    TabRow(selectedTabIndex = selectedTabIndex) {
                        Tab(
                            text = { Text(text = "Truths")},
                            selected = selectedTabIndex == 0,
                            onClick = { selectedTabIndex = 0 }
                        )
                        Tab(
                            text = { Text(text = "Dares")},
                            selected = selectedTabIndex == 1,
                            onClick = { selectedTabIndex = 1 }
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun TruthsList(truths: List<TruthOrDare>) {
    LazyColumn() {
        items(truths.size) { index ->
            val item = truths[index]

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

                IconButton(onClick = { /*TODO*/ }) {
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
fun DaresList(dares: List<TruthOrDare>) {
    LazyColumn() {
        items(dares.size) { index ->
            val item = dares[index]

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

                IconButton(onClick = { /*TODO*/ }) {
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
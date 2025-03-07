package com.example.curriculumtracker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.draw.clip
import android.util.Log
import androidx.compose.material.icons.filled.Close
import com.google.firebase.firestore.FirebaseFirestore




private fun parseFirestoreData(
    document: com.google.firebase.firestore.DocumentSnapshot,
    namesField: String,
    weeksField: String,
    progressField: String
): List<Triple<String, String, Float>> {
    val names = (document.get(namesField) as? List<*>)?.filterIsInstance<String>() ?: emptyList()
    val weeks = (document.get(weeksField) as? List<*>)?.filterIsInstance<Number>()?.map { "Week ${it.toInt()}" } ?: emptyList()
    val progress = (document.get(progressField) as? List<*>)?.filterIsInstance<Double>() ?: emptyList()

    return names.mapIndexed { index, name ->
        Triple(
            name,
            weeks.getOrNull(index) ?: "Unknown Week", // Safely handle weeks
            progress.getOrNull(index)?.toFloat() ?: 0f // Safely handle progress
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardUserScreen(
    track: String = "",
    onUserClick: (Int, String) -> Unit,
    onBack: () -> Unit,
    onNavbar: (String) -> Unit,
) {
    var selectedPage by remember { mutableStateOf(0) }
    var firstYearData by remember { mutableStateOf(emptyList<Triple<String, String, Float>>()) }
    var secondYearData by remember { mutableStateOf(emptyList<Triple<String, String, Float>>()) }
    var thirdYearData by remember { mutableStateOf(emptyList<Triple<String, String, Float>>()) }
    var fourthYearData by remember { mutableStateOf(emptyList<Triple<String, String, Float>>()) }

    val db = FirebaseFirestore.getInstance()

    LaunchedEffect(track) {
        if (track.isNotEmpty()) {
            db.collection(track)
                .document("users")
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        // Safely retrieve and parse data
                        firstYearData = parseFirestoreData(document, "n_1", "t_1", "p_1")
                        secondYearData = parseFirestoreData(document, "n_2", "t_2", "p_2")
                        thirdYearData = parseFirestoreData(document, "n_3", "t_3", "p_3")
                        fourthYearData = parseFirestoreData(document, "n_4", "t_4", "p_4")
                        Log.d("Firestore", "1st Year Data: $firstYearData")
                        Log.d("Firestore", "2nd Year Data: $secondYearData")
                        Log.d("Firestore", "3rd Year Data: $thirdYearData")
                        Log.d("Firestore", "4th Year Data: $fourthYearData")
                    } else {
                        Log.d("Firestore", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("Firestore", "Error fetching document", exception)
                }
        } else {
            Log.w("Firestore", "Track is empty")
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomAppBar(
                containerColor = Color(0xFF292929),
                modifier = Modifier.fillMaxWidth()
            ) {
                NavigationBar(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = Color(0xFF292929)
                ) {
                    NavigationBarItem(
                        selected = selectedPage == 0,
                        onClick = { selectedPage = 0;onNavbar("dashboard") },
                        label = { Text("Dashboard", color = Color.White) },
                        icon = {
                            Icon(
                                Icons.Default.Home,
                                contentDescription = "Home",
                                tint = if (selectedPage == 0) Color.Black else Color.White
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black, // Black icon when selected
                            unselectedIconColor = Color.Gray, // Gray icon when unselected
                            selectedTextColor = Color.Black, // Black text when selected
                            unselectedTextColor = Color.Gray, // Gray text when unselected
                            indicatorColor = Color(0xFFFFC107) // Yellow highlight for the selected page
                        )
                    )
                    NavigationBarItem(
                        selected = selectedPage == 1,
                        onClick = { selectedPage = 1;onNavbar("updates") },
                        label = { Text("My Updates", color = Color.White) },
                        icon = {
                            Icon(
                                Icons.Default.AccountCircle,
                                contentDescription = "Updates",
                                tint = if (selectedPage == 1) Color.Black else Color.White
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black, // Black icon when selected
                            unselectedIconColor = Color.Gray, // Gray icon when unselected
                            selectedTextColor = Color.Black, // Black text when selected
                            unselectedTextColor = Color.Gray, // Gray text when unselected
                            indicatorColor = Color(0xFFFFC107) // Yellow highlight for the selected page
                        )
                    )
                }
            }
        },
        topBar = {
            TopAppBar(
                title = { Text("$track Track") },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            imageVector = Icons.Default.Close, // The "X" icon
                            contentDescription = "Close",
                            tint = Color.White // Set the icon color
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF232323),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()) // Enable scrolling here
        ) {
            // Repeatable sections for each year
            Section(
                title = "1st Years",
                items = firstYearData,
                year = "1",
                onUserClick = onUserClick
            )
            Section(
                title = "2nd Years",
                items = secondYearData,
                year = "2",
                onUserClick = onUserClick
            )
            Section(
                title = "3rd Years",
                items = thirdYearData,
                year = "3",
                onUserClick = onUserClick
            )
            Section(
                title = "4th Years",
                items = fourthYearData,
                year = "4",
                onUserClick = onUserClick
            )
        }
    }
}

// Helper function for repeated sections
@Composable
fun Section(
    title: String,
    items: List<Triple<String, String, Float>>,
    year: String,
    onUserClick: (Int, String) -> Unit
) {
    Text(
        text = title,
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier.padding(vertical = 20.dp)
    )
    items.forEachIndexed { index, (name, work, progress) ->
        UserView(
            name = name,
            workingon = work,
            progress = progress,
            onClick = { onUserClick(index, year) } // Pass index and year when clicked
        )
    }
}

// UserView Composable remains the same
@Composable
fun UserView(
    name: String,
    workingon: String,
    progress: Float,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .clickable { onClick() }
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF292929))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween, // Ensure even spacing
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.weight(1f) // Distribute space proportionally
                )
                Text(
                    text = workingon,
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier.weight(1f) // Distribute space proportionally
                )
            }

            // Display progress percentage
            Text(
                text = "${(progress * 100).toInt()}% completed",
                fontSize = 14.sp,
                color = when {
                    progress > 0.75f -> Color(0xFF4CAF50)  // Green
                    progress > 0.50f -> Color(0xFFFFC107)  // Yellow
                    else -> Color(0xFFF44336)              // Red
                },
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Progress bar
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = when {
                    progress > 0.75f -> Color(0xFF4CAF50)  // Green
                    progress > 0.50f -> Color(0xFFFFC107)  // Yellow
                    else -> Color(0xFFF44336)              // Red
                },
                trackColor = Color(0xFF474747) // Background track color
            )
        }
    }
}

package com.example.curriculumtracker
import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.text.style.TextAlign
import com.google.firebase.firestore.FirebaseFirestore
import java.time.Year

private fun parseFirestoreData(
    document: com.google.firebase.firestore.DocumentSnapshot,
    namesField: String,
    weeksField: String,
    progressField: String,
    notesField: String
): List<Quadruple<String, String, Float, String>> {
    val names = (document.get(namesField) as? List<*>)?.filterIsInstance<String>() ?: emptyList()
    val weeks = (document.get(weeksField) as? List<*>)?.filterIsInstance<Number>()?.map { "Week ${it.toInt()}" } ?: emptyList()
    val progress = (document.get(progressField) as? List<*>)?.filterIsInstance<Number>()?.map { it.toFloat() } ?: emptyList()
    val notes = (document.get(notesField) as? List<*>)?.filterIsInstance<String>() ?: emptyList()

    return names.mapIndexed { index, name ->
        Quadruple(
            name,
            weeks.getOrNull(index) ?: "Unknown Week", // Convert numeric weeks to "Week N"
            progress.getOrNull(index) ?: 0f,         // Convert progress safely to Float
            notes.getOrNull(index) ?: "No Note Available" // Include notes
        )
    }
}

// Helper class to return multiple values
data class Quadruple<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserActivityScreen(
    track:String = "",
    year: String = "",
    index:Int = 0,
    onBack: () -> Unit,
    statDate: String = "1-2-2024",
    dayspent: Int = 5,
    description: String = "This is a placeholder description. ".repeat(50),
) {
    var userName by remember { mutableStateOf("") }
    var userNotes by remember { mutableStateOf("") }
    var userWeek by remember { mutableStateOf("") }
    var progress by remember { mutableStateOf(0.75f) }
    var Data by remember { mutableStateOf(emptyList<Triple<String, String, Float>>()) }
    val db = FirebaseFirestore.getInstance()

    // Fetch data from Firebase
    LaunchedEffect(track) {
        if (track.isNotEmpty() && year.isNotBlank()) {
            db.collection(track)
                .document("users")
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        // Safely retrieve and parse data
                        val Data = parseFirestoreData(document, "n_$year", "t_$year", "p_$year","nt_$year")
                        Log.d("Firestore", "Data: $Data")

                        // Retrieve the specific user's data based on the index
                        if (index in Data.indices) {
                            val userData = Data[index]
                            userName = userData.first
                            userWeek = userData.second
                            progress = userData.third
                            userNotes = userData.fourth
                        } else {
                            Log.w("Firestore", "Index out of bounds for Data list")
                        }
                    } else {
                        Log.d("Firestore", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("Firestore", "Error fetching document", exception)
                }
        } else {
            Log.w("Firestore", "Track or year is invalid")
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("User Activity") },
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
        ) {
            // User Name
            Text(
                textAlign = TextAlign.Center,
                text = userName,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
            )
            Text(
                textAlign = TextAlign.Center,
                text = "Currently on " + userWeek.lowercase(),
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
            )
            Spacer(Modifier.height(30.dp))

            // Task Description
            Text(
                text = "Task Description",
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Modern Description Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp)
                    .background(Color(0xFFFFC107), shape = RoundedCornerShape(24.dp)) // Heavily rounded corners
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = description,
                    fontSize = 16.sp,
                    color = Color.Black // Black text color for contrast
                )
            }

            Spacer(Modifier.height(30.dp))

            // Row for Start Date and Days Spent
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF232323), shape = RoundedCornerShape(24.dp)) // Heavily rounded corners
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Started: $statDate",
                    fontSize = 20.sp,
                    color = Color.White // Black text color
                )
                Text(
                    text = "Days spent: $dayspent",
                    fontSize = 20.sp,
                    color = Color.White // Black text color
                )
            }

            Spacer(Modifier.height(30.dp))

            // Progress Bar Section
            Text(
                text = "Progress: ${(progress * 100).toInt()}%",
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = when {
                    progress > 0.75f -> Color(0xFF4CAF50) // Green
                    progress > 0.50f -> Color(0xFFFFC107) // Yellow
                    else -> Color(0xFFF44336) // Red
                },
                trackColor = Color(0xFF474747),
            )

            Spacer(Modifier.height(30.dp))

            // Notes Section
            Text(
                text = "Notes:",
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp)
                    .background(Color(0xFFFFC107), shape = RoundedCornerShape(24.dp)) // Heavily rounded corners
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = if (userNotes.isEmpty()) "No Notes found.." else userNotes,
                    fontSize = 16.sp,
                    color = Color.Black // Black text color
                )
            }
        }
    }
}

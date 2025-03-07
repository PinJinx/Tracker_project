package com.example.curriculumtracker

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdatePage(
    current_user:FirebaseUser?,
    onNavbar: (String) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    var selectedPage by remember { mutableStateOf(1) }
    // UID and user data
    val uid = current_user?.uid
    var name by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    // Fetch name and year from Firebase
    LaunchedEffect(uid) {
        if (uid != null) {
            db.collection("Users")
                .document(uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        name = document.getString("name") ?: "Unknown Name"
                        year = document.getString("year") ?: "Unknown Year"
                        Log.d("FirebaseFetch", "Name: $name, Year: $year")
                    } else {
                        Log.d("FirebaseFetch", "No such document for UID: $uid")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("FirebaseFetch", "Error fetching user data for UID: $uid", exception)
                }
        }
    }

    // State variables for user input
    var selectedTrack by remember { mutableStateOf("") }
    var selectedWeek by remember { mutableStateOf(1) }
    var progress by remember { mutableStateOf(0f) }
    var notes by remember { mutableStateOf("") }

    // Sample tracks for the dropdown
    val tracks = listOf("Mobile", "AI", "Web", "System")
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
                        onClick = { selectedPage = 0; onNavbar("dashboard") },
                        label = { Text("Dashboard", color = Color.White) },
                        icon = {
                            Icon(
                                Icons.Default.Home,
                                contentDescription = "Home",
                                tint = if (selectedPage == 0) Color.Black else Color.White
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            unselectedIconColor = Color.Gray,
                            selectedTextColor = Color.Black,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color(0xFFFFC107)
                        )
                    )
                    NavigationBarItem(
                        selected = selectedPage == 1,
                        onClick = { selectedPage = 1; onNavbar("updates") },
                        label = { Text("My Updates", color = Color.White) },
                        icon = {
                            Icon(
                                Icons.Default.AccountCircle,
                                contentDescription = "Updates",
                                tint = if (selectedPage == 1) Color.Black else Color.White
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            unselectedIconColor = Color.Gray,
                            selectedTextColor = Color.Black,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color(0xFFFFC107)
                        )
                    )
                }
            }
        },
        topBar = {
            TopAppBar(
                title = { Text("My Updates") },
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
                .verticalScroll(rememberScrollState())
        ) {
            // Dropdown for Tracks
            Text(
                text = "Track",
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    value = selectedTrack,
                    onValueChange = {},
                    readOnly = true,
                    colors = TextFieldDefaults.textFieldColors(containerColor = Color.White),
                    placeholder = { Text("Select a track") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .menuAnchor(),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    }
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    tracks.forEach { track ->
                        DropdownMenuItem(
                            text = { Text(track) },
                            onClick = {
                                selectedTrack = track
                                expanded = false
                            }
                        )
                    }
                }
            }

// Week Selector Buttons
            Text(
                text = "Week",
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                // First Row: Buttons for Week 1 to 4
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    (1..4).forEach { week ->
                        Button(
                            onClick = { selectedWeek = week },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedWeek == week) Color(0xFFFFC107) else Color.DarkGray,
                                contentColor = if (selectedWeek == week) Color.Black else Color.White
                            ),
                            shape = RoundedCornerShape(12.dp), // Reduced curvature for more rectangular buttons
                            modifier = Modifier
                                .width(70.dp) // Make them wider for a rectangular look
                                .height(40.dp) // Height for a rectangular appearance
                        ) {
                            Text("$week", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Second Row: Buttons for Week 5 to 8
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    (5..8).forEach { week ->
                        Button(
                            onClick = { selectedWeek = week },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedWeek == week) Color(0xFFFFC107) else Color.DarkGray,
                                contentColor = if (selectedWeek == week) Color.Black else Color.White
                            ),
                            shape = RoundedCornerShape(12.dp), // Reduced curvature for more rectangular buttons
                            modifier = Modifier
                                .width(70.dp) // Make them wider for a rectangular look
                                .height(40.dp) // Height for a rectangular appearance
                        ) {
                            Text("$week", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Text(
                text = "Selected Week: $selectedWeek",
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )


            // Progress Slider
            Text(
                text = "Progress",
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Slider(
                value = progress,
                onValueChange = { progress = it },
                valueRange = 0f..1f,
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFFFFC107),
                    activeTrackColor = Color(0xFFFFC107),
                    inactiveTrackColor = Color.DarkGray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
            Text(
                text = "Progress: ${(progress * 100).toInt()}%",
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Notes Input Field
            Text(
                text = "Notes",
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TextField(
                value = notes,
                onValueChange = { notes = it },
                colors = TextFieldDefaults.textFieldColors(containerColor = Color.White),
                placeholder = { Text("Enter notes") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            Spacer(Modifier.height(20.dp))

            // Update Button
            Button(
                onClick = {
                    if (selectedTrack.isNotEmpty() && name.isNotEmpty() && notes.isNotEmpty()) {
                        val weekField = "t_$year"
                        val progressField = "p_$year"
                        val nameField = "n_$year"
                        val notesField = "nt_$year"

                        db.collection(selectedTrack)
                            .document("users")
                            .get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    // Retrieve the current lists from Firestore
                                    val names = document.get(nameField) as? List<String> ?: emptyList()
                                    val weeks = document.get(weekField) as? List<Number> ?: emptyList()
                                    val progresses = document.get(progressField) as? List<Number> ?: emptyList()
                                    val notesList = document.get(notesField) as? List<String> ?: emptyList()

                                    // Find the index of the user in the names list
                                    val userIndex = names.indexOf(name)
                                    if (userIndex != -1) {
                                        // Convert lists to mutable lists to update at specific index
                                        val updatedWeeks = weeks.toMutableList()
                                        val updatedProgresses = progresses.toMutableList()
                                        val updatedNotes = notesList.toMutableList()

                                        // Update the respective fields at the found index
                                        updatedWeeks[userIndex] = selectedWeek.toDouble()
                                        updatedProgresses[userIndex] = progress
                                        updatedNotes[userIndex] = notes

                                        // Write the updated data back to Firestore
                                        db.collection(selectedTrack)
                                            .document("users")
                                            .update(
                                                mapOf(
                                                    weekField to updatedWeeks,
                                                    progressField to updatedProgresses,
                                                    notesField to updatedNotes
                                                )
                                            )
                                            .addOnSuccessListener {
                                                Log.d("FirebaseUpdate", "User data successfully updated at index $userIndex")
                                            }
                                            .addOnFailureListener { e ->
                                                Log.e("FirebaseUpdate", "Error updating user data", e)
                                            }
                                    } else {
                                        Log.w("FirebaseUpdate", "User not found in the list")
                                    }
                                } else {
                                    Log.w("FirebaseUpdate", "No document found for the selected track")
                                }
                            }
                            .addOnFailureListener { e ->
                                Log.e("FirebaseUpdate", "Error fetching document", e)
                            }
                    } else {
                        Log.w("FirebaseUpdate", "All fields are required")
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Update",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

        }
    }
}

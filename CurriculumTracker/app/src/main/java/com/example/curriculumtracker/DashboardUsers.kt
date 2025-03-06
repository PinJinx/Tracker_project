package com.example.curriculumtracker
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
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.VerticalAlignmentLine





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardUserScreen() {
    var selectedPage by remember { mutableStateOf(0) }

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
                        onClick = { selectedPage = 0 },
                        label = { Text("Dashboard", color = Color.White) },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.White) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            unselectedIconColor = Color.Gray,
                            selectedTextColor = Color.White,
                            unselectedTextColor = Color.Gray
                        )
                    )
                    NavigationBarItem(
                        selected = selectedPage == 1,
                        onClick = { selectedPage = 1 },
                        label = { Text("My Updates", color = Color.White) },
                        icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Updates", tint = Color.White) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            unselectedIconColor = Color.Gray,
                            selectedTextColor = Color.White,
                            unselectedTextColor = Color.Gray
                        )
                    )
                }
            }
        },
        topBar = {
            TopAppBar(
                title = { Text("Placeholder Track") },
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
            Section(title = "1st Years", items = listOf(
                Triple("User 1", "Week 1", 0.8f),
                Triple("User 2", "Week 1", 0.6f),
                Triple("User 3", "Week 2", 0.5f),
                Triple("User 4", "Week 3", 0.3f)
            ))

            Section(title = "2nd Years", items = listOf(
                Triple("User 1", "Week 1", 0.8f),
                Triple("User 2", "Week 1", 0.6f),
                Triple("User 3", "Week 2", 0.5f),
                Triple("User 4", "Week 3", 0.3f)
            ))

            Section(title = "3rd Years", items = listOf(
                Triple("User 1", "Week 1", 0.8f),
                Triple("User 2", "Week 1", 0.6f),
                Triple("User 3", "Week 2", 0.5f),
                Triple("User 4", "Week 3", 0.3f)
            ))

            Section(title = "4th Years", items = listOf(
                Triple("User 1", "Week 1", 0.8f),
                Triple("User 2", "Week 1", 0.6f),
                Triple("User 3", "Week 2", 0.5f),
                Triple("User 4", "Week 3", 0.3f)
            ))
        }
    }
}

// Helper function for repeated sections
@Composable
fun Section(title: String, items: List<Triple<String, String, Float>>) {
    Text(
        text = title,
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier.padding(vertical = 20.dp)
    )
    items.forEach { (name, work, progress) ->
        UserView(
            name = name,
            workingon = work,
            progress = progress,
            onClick = {}
        )
    }
}
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
                progress = {progress},
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

@Preview(showBackground = true)
@Composable
fun DashboardUserScreenPreview() {
    DashboardUserScreen()
}

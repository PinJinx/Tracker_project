package com.example.curriculumtracker


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen() {
    var selectedPage by remember { mutableStateOf(0) }

    Scaffold(
        contentColor = Color(0xFF161616),
        bottomBar = {
            BottomAppBar(
                containerColor = Color(0xFF292929),
                modifier = Modifier.fillMaxWidth()
            ) {
                NavigationBar(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = Color(0xFF292929)  // Ensure the navigation bar's background is the same as the bottom bar
                ) {
                    NavigationBarItem(
                        selected = selectedPage == 0,
                        onClick = { selectedPage = 0 },
                        label = { Text("Dashboard", color = Color.White) },  // White text
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.White) },  // White icon
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
                        label = { Text("My Updates", color = Color.White) },  // White text
                        icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Updates", tint = Color.White) },  // White icon
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
                title = { Text("Dashboard") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF232323), titleContentColor = Color.White)
            )
        }

    )
    { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(padding)
                .padding(16.dp),
        ) {
            Text("Tracks", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(vertical = 20.dp))
            val tracks: List<Pair<String, Int>> = listOf(
                "Mobile" to android.R.drawable.ic_menu_gallery,
                "AI" to android.R.drawable.ic_menu_gallery,
                "Web" to android.R.drawable.ic_menu_gallery,
                "Systems" to android.R.drawable.ic_menu_gallery
            )
            // Display buttons in a grid layout
            Column {
                tracks.chunked(2).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        rowItems.forEach { (text, image) ->
                            DashboardButton(
                                text = text,
                                imageRes = image,
                                modifier = Modifier.weight(1f),
                                onClick = { /* Handle Click */ }
                            )
                        }
                    }
                }
            }

            Row() {   }
        }
    }
}



@Composable
fun DashboardButton(text: String, imageRes: Int, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF292929))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = text,
                modifier = Modifier.size(50.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    DashboardScreen()
}

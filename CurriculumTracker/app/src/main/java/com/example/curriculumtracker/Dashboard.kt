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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
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
fun DashboardScreen(
    onNavigateToUserActivity: () -> Unit,
    onNavbar: (String) -> Unit,
    setTrack: (String) -> Unit
) {
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
                            selectedIconColor = Color.Black, // Black icon when selected
                            unselectedIconColor = Color.Gray, // Gray icon when unselected
                            selectedTextColor = Color.Black, // Black text when selected
                            unselectedTextColor = Color.Gray, // Gray text when unselected
                            indicatorColor = Color(0xFFFFC107) // Yellow highlight for the selected page
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
                title = { Text("Dashboard") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF232323), titleContentColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(padding)
                .padding(16.dp),
        ) {
            Text("Tracks", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(vertical = 20.dp))

            val tracks: List<Pair<String, Int>> = listOf(
                "Mobile" to R.drawable.mobile_icon,
                "AI" to R.drawable.ai_icon,
                "Web" to R.drawable.web_icon,
                "System" to R.drawable.system_icon
            )

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
                                onClick = { onNavigateToUserActivity(); setTrack(text) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardButton(
    text: String,
    imageRes: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(8.dp)
            .clickable(onClick = onClick)
            .background(Color.Black, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(16.dp)) // Add corner radius
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = 0.5f } // Set opacity to 50%
        )
        Text(
            text = text,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

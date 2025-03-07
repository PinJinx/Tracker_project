package com.example.curriculumtracker

import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.firebase.auth.FirebaseAuth
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        setContent {
            // Use a mutable state to track the current screen
            var currentScreen by remember { mutableStateOf(if (currentUser != null) "dashboard" else "login") }
            var currentTrack by remember { mutableStateOf("") }
            var currentUserYear by remember { mutableStateOf("") }
            var currentUserIndex by remember { mutableStateOf(0) }
            when (currentScreen) {
                "login" -> {
                    LoginScreen(
                        onLoginSuccess = { currentScreen = "dashboard" } // Switch to Dashboard on login success
                    )
                }
                "dashboard" -> {
                    DashboardScreen(
                        onNavigateToUserActivity = { currentScreen = "dashboardUser" },
                        setTrack = { track -> currentTrack = track},
                        onNavbar = {bar -> currentScreen = bar}// Correct the lambda to update `currentTrack`
                    )
                }
                "dashboardUser" -> {
                    DashboardUserScreen(onBack = {currentScreen = "dashboard"},onNavbar = {bar -> currentScreen = bar},track = currentTrack, onUserClick = { index,yr -> currentUserIndex = index;currentUserYear = yr; currentScreen = "useractivity"})
                }
                "useractivity" -> {
                    UserActivityScreen(onBack = { currentScreen = "dashboardUser" }, track = currentTrack, year = currentUserYear, index = currentUserIndex)
                }
                "updates" ->{
                    UpdatePage(current_user = currentUser,onNavbar = {bar -> currentScreen = bar})
                }
            }
        }
    }
}


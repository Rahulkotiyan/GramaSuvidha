package com.example.gramasuvidha.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gramasuvidha.auth.AuthService

@Composable
fun AuthenticatedFeedbackWrapper(
    projectId: String?,
    authService: AuthService,
    onNavigateBack: () -> Unit
) {
    val authState by authService.authState.collectAsState()
    
    if (authState.isAuthenticated) {
        // Show feedback screen for authenticated users
        EnhancedFeedbackScreen(
            projectId = projectId,
            userId = authState.user?.id,
            onNavigateBack = onNavigateBack
        )
    } else {
        // Show login prompt for unauthenticated users
        LoginPromptScreen(
            onNavigateBack = onNavigateBack,
            onLoginSuccess = { /* Refresh the screen */ }
        )
    }
}

@Composable
private fun LoginPromptScreen(
    onNavigateBack: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Login Required",
                    style = MaterialTheme.typography.headlineMedium
                )
                
                Text(
                    text = "Please login to submit feedback",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = onNavigateBack
                    ) {
                        Text("Back")
                    }
                    
                    Button(
                        onClick = onLoginSuccess
                    ) {
                        Text("Login")
                    }
                }
            }
        }
    }
}

@Composable
private fun EnhancedFeedbackScreen(
    projectId: String?,
    userId: String?,
    onNavigateBack: () -> Unit
) {
    // Use existing EnhancedFeedbackScreen
    com.example.gramasuvidha.ui.screens.EnhancedFeedbackScreen(
        projectId = projectId,
        onNavigateBack = onNavigateBack
    )
}

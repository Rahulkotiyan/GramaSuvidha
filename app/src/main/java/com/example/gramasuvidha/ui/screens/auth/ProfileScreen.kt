package com.example.gramasuvidha.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gramasuvidha.auth.AuthService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
        authService: AuthService,
        onNavigateBack: () -> Unit = {},
        onNavigateToLogin: () -> Unit = {}
) {
    val authState by authService.authState.collectAsState()
    val user = authState.user

    Scaffold(
            topBar = {
                TopAppBar(
                        title = { Text("Profile") },
                        navigationIcon = {
                            IconButton(onClick = onNavigateBack) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                            }
                        }
                )
            }
    ) { padding ->
        Column(
                modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // User Info Card
            Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                                Icons.Default.Person,
                                contentDescription = "Profile",
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.primary
                        )

                        Column {
                            Text(
                                    text = user?.fullName ?: "User",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
                            )
                            Text(
                                    text = user?.email ?: "",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }

                    if (!user?.phone.isNullOrBlank()) {
                        Text(
                                text = "Phone: ${user?.phone}",
                                style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    if (!user?.village.isNullOrBlank()) {
                        Text(
                                text = "Village: ${user?.village}",
                                style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // Logout Button
            Button(
                    onClick = {
                        authService.logout()
                        onNavigateToLogin()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors =
                            ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error
                            )
            ) {
                Icon(Icons.Default.Logout, contentDescription = "Logout")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Logout")
            }
        }
    }
}

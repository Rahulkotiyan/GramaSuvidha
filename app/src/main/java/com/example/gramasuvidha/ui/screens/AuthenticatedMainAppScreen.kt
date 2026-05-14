package com.example.gramasuvidha.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import com.example.gramasuvidha.R
import com.example.gramasuvidha.auth.AuthService
import com.example.gramasuvidha.ui.screens.auth.AuthenticatedFeedbackWrapper
import com.example.gramasuvidha.ui.screens.auth.LoginScreen
import com.example.gramasuvidha.ui.screens.auth.RegisterScreen
import com.example.gramasuvidha.ui.screens.auth.ProfileScreen
import com.example.gramasuvidha.viewmodels.ProjectViewModel

@Composable
fun AuthenticatedMainAppScreen(viewModel: ProjectViewModel) {
    val context = LocalContext.current
    val navController = rememberNavController()
    
    // Initialize AuthService
    val authService = remember { AuthService(context) }
    val authState by authService.authState.collectAsState()
    
    val configuration = LocalConfiguration.current
    
    // Bottom Navigation Setup with Login Tab
    val items = listOf("notice_board", "dashboard", "feedback", "login", "settings")
    
    val icons = listOf(
        Icons.AutoMirrored.Filled.List,
        Icons.Default.Info,
        Icons.Default.Email,
        Icons.Default.Person,
        Icons.Default.Settings
    )
    
    val labels = listOf(
        stringResource(R.string.tab_notice_board),
        stringResource(R.string.tab_dashboard),
        stringResource(R.string.tab_feedback),
        if (authState.isAuthenticated) "Profile" else "Login",
        stringResource(R.string.tab_settings)
    )
    
    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier.shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp
                    )
                ).background(MaterialTheme.colorScheme.surface),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                tonalElevation = 0.dp
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route?.substringBefore("/")
                
                items.forEachIndexed { index, screenRoute ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                icons[index],
                                contentDescription = labels[index],
                                tint = if (currentRoute == screenRoute)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                modifier = Modifier
                            )
                        },
                        label = {
                            Text(
                                labels[index],
                                color = if (currentRoute == screenRoute)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        selected = currentRoute == screenRoute,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                            unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        ),
                        onClick = {
                            navController.navigate(screenRoute) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "notice_board",
            modifier = Modifier.padding(innerPadding)
        ) {
            // Existing screens
            composable("notice_board") { 
                NoticeBoardScreen(navController) 
            }
            
            composable("dashboard") { 
                DashboardScreen(viewModel, navController) 
            }
            
            // Enhanced feedback with authentication
            composable("feedback") { 
                AuthenticatedFeedbackWrapper(
                    projectId = null,
                    authService = authService,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            // Login/Profile Tab
            composable("login") {
                if (authState.isAuthenticated) {
                    // Show Profile Screen when logged in
                    ProfileScreen(
                        authService = authService,
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToLogin = { 
                            navController.navigate("login") 
                        }
                    )
                } else {
                    // Show Login Screen when not logged in
                    LoginScreen(
                        authService = authService,
                        onLoginSuccess = { 
                            navController.navigate("login") // Refresh to show profile
                        },
                        onNavigateToRegister = { 
                            navController.navigate("register") 
                        },
                        onNavigateToForgotPassword = { 
                            // TODO: Implement forgot password navigation
                        }
                    )
                }
            }
            
            // Register Screen
            composable("register") {
                RegisterScreen(
                    authService = authService,
                    onRegisterSuccess = { 
                        navController.navigate("login") // Go back to login tab to show profile
                    },
                    onNavigateToLogin = { 
                        navController.popBackStack()
                    }
                )
            }
            
            composable("settings") { 
                SettingsScreen() 
            }
            
            composable("project_detail/{projectId}") { backStackEntry ->
                val projectId = backStackEntry.arguments?.getString("projectId")
                ProjectDetailScreen(projectId, viewModel, navController)
            }
        }
    }
}

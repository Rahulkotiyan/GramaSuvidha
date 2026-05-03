package com.example.gramasuvidha.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.gramasuvidha.viewmodels.ProjectViewModel

@Composable
fun MainAppScreen(viewModel: ProjectViewModel) {
    val navController = rememberNavController()

    // Bottom Navigation Setup
    val items = listOf("notice_board", "dashboard", "feedback", "settings")
    val icons = listOf(Icons.Default.List, Icons.Default.Info, Icons.Default.Email, Icons.Default.Settings)
    val labels = listOf("Notice Board", "Dashboard", "Feedback", "Settings")

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route?.substringBefore("/")

                items.forEachIndexed { index, screenRoute ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = labels[index]) },
                        label = { Text(labels[index]) },
                        selected = currentRoute == screenRoute,
                        onClick = {
                            navController.navigate(screenRoute) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
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
            composable("notice_board") { NoticeBoardScreen(viewModel, navController) }
            composable("dashboard") { DashboardScreen(viewModel) }
            composable("feedback") { FeedbackScreen() }
            composable("settings") { SettingsScreen() }
            composable("project_detail/{projectId}") { backStackEntry ->
                val projectId = backStackEntry.arguments?.getString("projectId")
                ProjectDetailScreen(projectId, viewModel, navController)
            }
        }
    }
}
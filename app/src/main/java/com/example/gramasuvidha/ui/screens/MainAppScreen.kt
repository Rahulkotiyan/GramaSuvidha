package com.example.gramasuvidha.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.*
import com.example.gramasuvidha.R
import com.example.gramasuvidha.viewmodels.ProjectViewModel

@Composable
fun MainAppScreen(viewModel: ProjectViewModel) {
    val navController = rememberNavController()

    // This makes the composable recompose when configuration (locale) changes
    val configuration = LocalConfiguration.current

    // Bottom Navigation Setup
    val items = listOf("notice_board", "dashboard", "feedback", "settings")
    val icons =
            listOf(
                    Icons.AutoMirrored.Filled.List,
                    Icons.Default.Info,
                    Icons.Default.Email,
                    Icons.Default.Settings
            )
    val labels =
            listOf(
                    stringResource(R.string.tab_notice_board),
                    stringResource(R.string.tab_dashboard),
                    stringResource(R.string.tab_feedback),
                    stringResource(R.string.tab_settings)
            )

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
            // Your ViewModel is properly passed to the screens here!
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

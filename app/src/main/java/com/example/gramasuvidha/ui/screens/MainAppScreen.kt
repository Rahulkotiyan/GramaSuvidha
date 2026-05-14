package com.example.gramasuvidha.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
                NavigationBar(
                        modifier =
                                Modifier.shadow(
                                                elevation = 12.dp,
                                                shape =
                                                        RoundedCornerShape(
                                                                topStart = 20.dp,
                                                                topEnd = 20.dp
                                                        )
                                        )
                                        .background(MaterialTheme.colorScheme.surface),
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
                                            tint =
                                                    if (currentRoute == screenRoute)
                                                            MaterialTheme.colorScheme.primary
                                                    else
                                                            MaterialTheme.colorScheme.onSurface
                                                                    .copy(alpha = 0.5f),
                                            modifier =
                                                    Modifier.run {
                                                        if (currentRoute == screenRoute) {

                                                            this
                                                        } else {

                                                            this
                                                        }
                                                    }
                                    )
                                },
                                label = {
                                    Text(
                                            labels[index],
                                            color =
                                                    if (currentRoute == screenRoute)
                                                            MaterialTheme.colorScheme.primary
                                                    else
                                                            MaterialTheme.colorScheme.onSurface
                                                                    .copy(alpha = 0.5f),
                                            style = MaterialTheme.typography.labelSmall
                                    )
                                },
                                selected = currentRoute == screenRoute,
                                colors =
                                        NavigationBarItemDefaults.colors(
                                                selectedIconColor =
                                                        MaterialTheme.colorScheme.primary,
                                                selectedTextColor =
                                                        MaterialTheme.colorScheme.primary,
                                                indicatorColor =
                                                        MaterialTheme.colorScheme.primary.copy(
                                                                alpha = 0.12f
                                                        ),
                                                unselectedIconColor =
                                                        MaterialTheme.colorScheme.onSurface.copy(
                                                                alpha = 0.5f
                                                        ),
                                                unselectedTextColor =
                                                        MaterialTheme.colorScheme.onSurface.copy(
                                                                alpha = 0.5f
                                                        )
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

            // Your ViewModel is properly passed to the screens here!

            composable("notice_board") { NoticeBoardScreen(navController) }

            composable("dashboard") { DashboardScreen(viewModel, navController) }

            composable("feedback") { FeedbackScreen() }

            composable("settings") { SettingsScreen() }

            composable("project_detail/{projectId}") { backStackEntry ->
                val projectId = backStackEntry.arguments?.getString("projectId")

                ProjectDetailScreen(projectId, viewModel, navController)
            }
        }
    }
}

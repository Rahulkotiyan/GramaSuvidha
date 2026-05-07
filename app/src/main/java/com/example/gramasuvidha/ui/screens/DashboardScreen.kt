package com.example.gramasuvidha.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gramasuvidha.R
import com.example.gramasuvidha.models.Project
import com.example.gramasuvidha.utils.getLocalizedTitle
import com.example.gramasuvidha.viewmodels.ProjectViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: ProjectViewModel, navController: NavController) {
    val projects by viewModel.projects.collectAsState()
    val context = LocalContext.current
    android.util.Log.d("GramaSuvidha", "DashboardScreen: Rendering with ${projects.size} projects")
    val totalProjects = projects.size
    val completedProjects = projects.count { it.progress_percentage == 100 }
    val recentProjects = projects.take(5) // Show first 5 projects

    Scaffold(
            topBar = {
                TopAppBar(
                        title = {
                            Text(
                                    stringResource(R.string.dashboard_title),
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.headlineSmall
                            )
                        },
                        colors =
                                TopAppBarDefaults.topAppBarColors(
                                        containerColor = MaterialTheme.colorScheme.surface,
                                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                                        scrolledContainerColor = MaterialTheme.colorScheme.surface
                                )
                )
            }
    ) { padding ->
        Column(
                modifier =
                        Modifier.fillMaxSize()
                                .padding(padding)
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Quick Stats Row
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Total Projects Card
                StatCard(
                        modifier = Modifier.weight(1f),
                        title = stringResource(R.string.total_projects_label),
                        value = "$totalProjects",
                        icon = Icons.Default.Assignment,
                        iconColor = MaterialTheme.colorScheme.primary
                )

                // Completed Projects Card
                StatCard(
                        modifier = Modifier.weight(1f),
                        title = stringResource(R.string.completed_projects_label),
                        value = "$completedProjects",
                        icon = Icons.Default.CheckCircle,
                        iconColor = MaterialTheme.colorScheme.primary
                )
            }

            // Progress Overview Card
            Card(
                    modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors =
                            CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                            ),
                    border = CardDefaults.outlinedCardBorder().copy(width = 1.dp)
            ) {
                Column(modifier = Modifier.padding(22.dp)) {
                    Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                                text = "Overall Progress",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                        )
                        Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = "Progress",
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                                modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    if (totalProjects > 0) {
                        val completionRate =
                                (completedProjects.toFloat() / totalProjects * 100).toInt()

                        // Circular Progress Indicator
                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                        text = "$completionRate%",
                                        style = MaterialTheme.typography.displaySmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontSize = 48.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                        text = "Complete",
                                        style = MaterialTheme.typography.bodySmall,
                                        color =
                                                MaterialTheme.colorScheme.onSurface.copy(
                                                        alpha = 0.7f
                                                ),
                                        fontWeight = FontWeight.Medium
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            LinearProgressIndicator(
                                    progress = { completionRate / 100f },
                                    modifier = Modifier.width(8.dp).height(120.dp),
                                    color = MaterialTheme.colorScheme.primary,
                                    trackColor =
                                            MaterialTheme.colorScheme.surfaceVariant.copy(
                                                    alpha = 0.5f
                                            )
                            )
                        }
                    } else {
                        Text(
                                text = "No projects yet",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Summary Section
            Card(
                    modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors =
                            CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                            ),
                    border = CardDefaults.outlinedCardBorder().copy(width = 1.dp)
            ) {
                Column(modifier = Modifier.padding(22.dp)) {
                    Text(
                            text = "Summary",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    SummaryItem(
                            label = stringResource(R.string.total_projects_label),
                            value = "$totalProjects",
                            icon = Icons.Default.Assignment
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    SummaryItem(
                            label = stringResource(R.string.completed_projects_label),
                            value = "$completedProjects",
                            icon = Icons.Default.CheckCircle
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    SummaryItem(
                            label = "In Progress",
                            value = "${totalProjects - completedProjects}",
                            icon = Icons.Default.Pending
                    )
                }
            }

            // Recent Projects Section
            if (recentProjects.isNotEmpty()) {
                Card(
                        modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors =
                                CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surface
                                ),
                        border = CardDefaults.outlinedCardBorder().copy(width = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(22.dp)) {
                        Text(
                                text = "Recent Projects",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        recentProjects.forEach { project ->
                            val localizedTitle = getLocalizedTitle(context, project)
                            DashboardProjectItem(
                                    project = project,
                                    title = localizedTitle,
                                    onClick = { 
                                        android.util.Log.d("GramaSuvidha", "DashboardScreen: Clicked project ${project.project_id}, navigating to details")
                                        navController.navigate("project_detail/${project.project_id}")
                                    }
                            )
                            if (project != recentProjects.last()) {
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(
        modifier: Modifier = Modifier,
        title: String,
        value: String,
        icon: ImageVector,
        iconColor: androidx.compose.ui.graphics.Color
) {
    Card(
            modifier = modifier.shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = CardDefaults.outlinedCardBorder().copy(width = 1.dp)
    ) {
        Column(
                modifier = Modifier.padding(18.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor.copy(alpha = 0.8f),
                    modifier = Modifier.size(28.dp)
            )

            Text(
                    text = value,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 26.sp
            )

            Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun SummaryItem(label: String, value: String, icon: ImageVector) {
    Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                    modifier = Modifier.size(20.dp)
            )
            Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Medium
            )
        }
        Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun DashboardProjectItem(project: Project, title: String, onClick: () -> Unit) {
    Row(
            modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick() }
                    .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                        text = project.budget,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                        text = "•",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
                Text(
                        text = "${project.progress_percentage}%",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (project.progress_percentage == 100) 
                            MaterialTheme.colorScheme.primary 
                        else 
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
        Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "View Details",
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                modifier = Modifier.size(20.dp)
        )
    }
}

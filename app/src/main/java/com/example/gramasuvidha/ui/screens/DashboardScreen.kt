package com.example.gramasuvidha.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gramasuvidha.viewmodels.ProjectViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: ProjectViewModel) {
    val projects by viewModel.projects.collectAsState()
    val totalProjects = projects.size
    val completedProjects = projects.count { it.progress_percentage == 100 }

    Scaffold(topBar = { TopAppBar(title = { Text("Village Dashboard") }) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Total Active Projects", style = MaterialTheme.typography.titleMedium)
                    Text("$totalProjects", style = MaterialTheme.typography.displayMedium)
                }
            }
            Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Completed Works", style = MaterialTheme.typography.titleMedium)
                    Text("$completedProjects", style = MaterialTheme.typography.displayMedium)
                }
            }
        }
    }
}
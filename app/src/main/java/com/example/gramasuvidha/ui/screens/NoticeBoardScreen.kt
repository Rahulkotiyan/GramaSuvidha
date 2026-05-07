package com.example.gramasuvidha.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gramasuvidha.R
import com.example.gramasuvidha.models.Project
import com.example.gramasuvidha.utils.getLocalizedTitle
import com.example.gramasuvidha.viewmodels.ProjectViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoticeBoardScreen(viewModel: ProjectViewModel, navController: NavController) {
    val projects by viewModel.projects.collectAsState()
    android.util.Log.d("GramaSuvidha", "NoticeBoardScreen: Rendering with ${projects.size} projects")

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.notice_board_title)) }) }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            items(projects) { project ->
                ProjectCard(project = project, onClick = {
                    navController.navigate("project_detail/${project.project_id}")
                })
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun ProjectCard(project: Project, onClick: () -> Unit) {
    val context = LocalContext.current
    val localizedTitle = getLocalizedTitle(context, project)
    
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = localizedTitle, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "${stringResource(R.string.budget_label)}${project.budget}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))

            // Progress Section
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(stringResource(R.string.progress_label), style = MaterialTheme.typography.labelMedium)
                Text("${project.progress_percentage}%", style = MaterialTheme.typography.labelMedium)
            }
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { project.progress_percentage / 100f },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

package com.example.gramasuvidha.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.gramasuvidha.R
import com.example.gramasuvidha.utils.getLocalizedDescription
import com.example.gramasuvidha.utils.getLocalizedTitle
import com.example.gramasuvidha.viewmodels.ProjectViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreen(
        projectId: String?,
        viewModel: ProjectViewModel,
        navController: NavController
) {
    val projects by viewModel.projects.collectAsState()
    val project = projects.find { it.project_id == projectId }
    val context = LocalContext.current

    Scaffold(
            topBar = {
                TopAppBar(
                        title = { Text(stringResource(R.string.project_details_title)) },
                        navigationIcon = {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back"
                                )
                            }
                        }
                )
            }
    ) { padding ->
        if (project == null) {
            Text(stringResource(R.string.project_not_found), modifier = Modifier.padding(padding))
            return@Scaffold
        }

        val localizedTitle = getLocalizedTitle(context, project)
        val localizedDescription = getLocalizedDescription(context, project)

        Column(
                modifier =
                        Modifier.padding(padding)
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState())
        ) {
            Text(
                    text = localizedTitle,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
            )
            Text(
                    text = project.title_kn,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = localizedDescription, style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                            "${stringResource(R.string.budget_label)}${project.budget}",
                            fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                            "${stringResource(R.string.current_status_label)}: ${project.progress_percentage}${stringResource(R.string.complete_label)}"
                    )
                    LinearProgressIndicator(
                            progress = { project.progress_percentage / 100f },
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                    stringResource(R.string.site_photos_label),
                    style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Using Coil to load images (Will show empty/placeholder if URL is mock)
            AsyncImage(
                    model = project.images.current_url,
                    contentDescription = stringResource(R.string.current_project_state),
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    contentScale = ContentScale.Crop
            )
        }
    }
}

package com.example.gramasuvidha.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.gramasuvidha.R
import com.example.gramasuvidha.utils.getLocalizedDescription
import com.example.gramasuvidha.utils.getLocalizedTitle
import com.example.gramasuvidha.viewmodels.ProjectViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedProjectDetailScreen(
    projectId: String?,
    viewModel: ProjectViewModel,
    navController: NavController
) {
    val projects by viewModel.projects.collectAsState()
    android.util.Log.d("GramaSuvidha", "EnhancedProjectDetailScreen: Looking for project $projectId, available projects: ${projects.size}")
    val project = projects.find { it.project_id == projectId }
    android.util.Log.d("GramaSuvidha", "EnhancedProjectDetailScreen: Found project: ${project != null}")
    val context = LocalContext.current
    
    // Return early if project is null to prevent crashes
    if (project == null) {
        android.util.Log.e("GramaSuvidha", "EnhancedProjectDetailScreen: Project not found for ID: $projectId")
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.project_details_title),
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        if (project == null) {
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    stringResource(R.string.project_not_found),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            return@Scaffold
        }

        val localizedTitle = getLocalizedTitle(context, project)
        val localizedDescription = getLocalizedDescription(context, project)
        
        android.util.Log.d("GramaSuvidha", "EnhancedProjectDetailScreen: Successfully loaded localized content")

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Title Section
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = localizedTitle,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 28.sp
                )
                Text(
                    text = project.title_kn,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontWeight = FontWeight.Normal
                )
            }

            // Description Card
            Card(
                modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = CardDefaults.outlinedCardBorder().copy(width = 1.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Description",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = localizedDescription,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        lineHeight = 20.sp
                    )
                }
            }

            // Project Details Card
            Card(
                modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = CardDefaults.outlinedCardBorder().copy(width = 1.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Budget",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            project.budget,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Divider(
                        modifier = Modifier.padding(vertical = 14.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        thickness = 1.dp
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Status",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "${project.progress_percentage}% Complete",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LinearProgressIndicator(
                        progress = { project.progress_percentage / 100f },
                        modifier = Modifier.fillMaxWidth().height(6.dp),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                }
            }

            // Image Section - Phase 4: Real Network Images with Coil
            Text(
                "Project Photos",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Before Image
            Text(
                "Before Work Started",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            
            Card(
                modifier = Modifier.fillMaxWidth().height(200.dp).shadow(4.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(project.before_url)
                        .crossfade(true)
                        .memoryCachePolicy(coil.request.CachePolicy.ENABLED)
                        .diskCachePolicy(coil.request.CachePolicy.ENABLED)
                        .build(),
                    loading = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(32.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    error = {
                        // Fallback to placeholder image on error
                        val fallbackImage = when {
                            project.title_en.contains("Road", ignoreCase = true) -> 
                                com.example.gramasuvidha.R.drawable.project_placeholder_road
                            project.title_en.contains("Borewell", ignoreCase = true) || 
                            project.title_en.contains("Water", ignoreCase = true) -> 
                                com.example.gramasuvidha.R.drawable.project_placeholder_water
                            project.title_en.contains("Hall", ignoreCase = true) || 
                            project.title_en.contains("School", ignoreCase = true) ||
                            project.title_en.contains("Center", ignoreCase = true) ||
                            project.title_en.contains("Health", ignoreCase = true) -> 
                                com.example.gramasuvidha.R.drawable.project_placeholder_building
                            else -> 
                                com.example.gramasuvidha.R.drawable.project_placeholder_general
                        }
                        
                        Image(
                            painter = painterResource(id = fallbackImage),
                            contentDescription = "Before work - placeholder",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    },
                    contentDescription = "Before work started",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Current Image
            Text(
                "Current Status",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            
            Card(
                modifier = Modifier.fillMaxWidth().height(200.dp).shadow(4.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(project.current_url)
                        .crossfade(true)
                        .memoryCachePolicy(coil.request.CachePolicy.ENABLED)
                        .diskCachePolicy(coil.request.CachePolicy.ENABLED)
                        .build(),
                    loading = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(32.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    error = {
                        // Fallback to placeholder image on error
                        val fallbackImage = when {
                            project.title_en.contains("Road", ignoreCase = true) -> 
                                com.example.gramasuvidha.R.drawable.project_placeholder_road
                            project.title_en.contains("Borewell", ignoreCase = true) || 
                            project.title_en.contains("Water", ignoreCase = true) -> 
                                com.example.gramasuvidha.R.drawable.project_placeholder_water
                            project.title_en.contains("Hall", ignoreCase = true) || 
                            project.title_en.contains("School", ignoreCase = true) ||
                            project.title_en.contains("Center", ignoreCase = true) ||
                            project.title_en.contains("Health", ignoreCase = true) -> 
                                com.example.gramasuvidha.R.drawable.project_placeholder_building
                            else -> 
                                com.example.gramasuvidha.R.drawable.project_placeholder_general
                        }
                        
                        Image(
                            painter = painterResource(id = fallbackImage),
                            contentDescription = "Current status - placeholder",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    },
                    contentDescription = "Current project status",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

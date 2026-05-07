package com.example.gramasuvidha.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gramasuvidha.data.AppDatabase
import com.example.gramasuvidha.data.ProjectRepository
import com.example.gramasuvidha.models.Project
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProjectViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProjectRepository
    val projects: StateFlow<List<Project>>

    init {
        try {
            Log.d("GramaSuvidha", "ProjectViewModel: Initializing...")
            
            // Initialize Database and Repository
            val database = AppDatabase.getDatabase(application)
            Log.d("GramaSuvidha", "ProjectViewModel: Database initialized")
            
            repository = ProjectRepository(database.projectDao(), application)
            Log.d("GramaSuvidha", "ProjectViewModel: Repository initialized")

            // Convert the Room Flow into a StateFlow so the Compose UI can read it
            projects = repository.allProjects.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
            Log.d("GramaSuvidha", "ProjectViewModel: StateFlow initialized")

            // Launch a coroutine to seed the database with JSON data if it's empty
            viewModelScope.launch {
                try {
                    Log.d("GramaSuvidha", "ProjectViewModel: Starting database seed...")
                    repository.seedDatabaseIfNeeded()
                    Log.d("GramaSuvidha", "ProjectViewModel: Database seeding complete")
                } catch (e: Exception) {
                    Log.e("GramaSuvidha", "ProjectViewModel: Error seeding database", e)
                }
            }
            
            Log.d("GramaSuvidha", "ProjectViewModel: Initialization complete")
        } catch (e: Exception) {
            Log.e("GramaSuvidha", "ProjectViewModel: Error during initialization", e)
            throw e
        }
    }
}
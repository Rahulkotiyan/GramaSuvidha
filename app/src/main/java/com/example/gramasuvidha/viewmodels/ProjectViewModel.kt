package com.example.gramasuvidha.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gramasuvidha.data.AppDatabase
import com.example.gramasuvidha.data.ProjectRepository
import com.example.gramasuvidha.models.Project
import com.example.gramasuvidha.network.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Phase 3: Enhanced ProjectViewModel with Network Support
 * Handles both local database operations and network synchronization
 */
class ProjectViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "ProjectViewModel"
    }

    private val repository: ProjectRepository
    val projects: StateFlow<List<Project>>
    
    // Phase 3: Network state management
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()
    
    private val _syncStatus = MutableStateFlow<String?>(null)
    val syncStatus: StateFlow<String?> = _syncStatus.asStateFlow()

    init {
        try {
            Log.d(TAG, "Initializing ProjectViewModel...")
            
            // Initialize Database and Repository
            val database = AppDatabase.getDatabase(application)
            Log.d(TAG, "Database initialized")
            
            repository = ProjectRepository(database.projectDao(), application)
            Log.d(TAG, "Repository initialized")

            // Convert the Room Flow into a StateFlow so the Compose UI can read it
            projects = repository.allProjects.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
            Log.d(TAG, "StateFlow initialized")

            // Launch a coroutine to initialize the repository with network sync
            viewModelScope.launch {
                try {
                    Log.d(TAG, "Starting repository initialization...")
                    _syncStatus.value = "Initializing..."
                    repository.seedDatabaseIfNeeded()
                    _syncStatus.value = null
                    Log.d(TAG, "Repository initialization complete")
                } catch (e: Exception) {
                    Log.e(TAG, "Error during repository initialization", e)
                    _syncStatus.value = "Initialization failed: ${e.message}"
                }
            }
            
            Log.d(TAG, "ProjectViewModel initialization complete")
        } catch (e: Exception) {
            Log.e(TAG, "Error during initialization", e)
            _syncStatus.value = "Initialization error: ${e.message}"
            throw e
        }
    }
    
    /**
     * Phase 3: Refresh projects from API
     * This can be called by the UI when user pulls to refresh
     */
    fun refreshProjects() {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Starting manual refresh...")
                _isRefreshing.value = true
                _syncStatus.value = "Syncing with server..."
                
                val result = repository.refreshProjects()
                
                when (result) {
                    is NetworkResult.Success -> {
                        Log.d(TAG, "Refresh successful: ${result.data.size} projects")
                        _syncStatus.value = "Synced ${result.data.size} projects"
                    }
                    is NetworkResult.Error -> {
                        Log.w(TAG, "Refresh failed: ${result.message}")
                        _syncStatus.value = "Sync failed: ${result.message}"
                    }
                    is NetworkResult.Loading -> {
                        _syncStatus.value = "Syncing..."
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error during refresh", e)
                _syncStatus.value = "Refresh error: ${e.message}"
            } finally {
                _isRefreshing.value = false
                // Clear status message after 3 seconds
                kotlinx.coroutines.delay(3000)
                _syncStatus.value = null
            }
        }
    }
    
    /**
     * Phase 3: Get cached projects count
     */
    suspend fun getCachedProjectsCount(): Int {
        return repository.getCachedProjectsCount()
    }
    
    /**
     * Phase 3: Check if we have cached data
     */
    suspend fun hasCachedData(): Boolean {
        return repository.hasCachedData()
    }
    
    /**
     * Phase 3: Get a specific project by ID
     */
    suspend fun getProjectById(projectId: String): Project? {
        return repository.getProjectById(projectId)
    }
    
    /**
     * Phase 3: Clear sync status
     */
    fun clearSyncStatus() {
        _syncStatus.value = null
    }
}
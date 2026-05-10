package com.example.gramasuvidha.data

import android.content.Context
import android.util.Log
import com.example.gramasuvidha.models.Project
import com.example.gramasuvidha.models.ProjectDao
import com.example.gramasuvidha.network.NetworkRepository
import com.example.gramasuvidha.network.NetworkResult
import com.example.gramasuvidha.network.RetrofitClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

/**
 * Phase 3: Updated ProjectRepository with Network Integration
 * This repository now acts as the single source of truth, mediating between
 * the Room database, Network API, and ViewModels
 */
class ProjectRepository(
    private val projectDao: ProjectDao, 
    private val context: Context
) {
    
    companion object {
        private const val TAG = "ProjectRepository"
    }
    
    // Phase 3: Network repository for API operations
    private val networkRepository: NetworkRepository by lazy {
        NetworkRepository(
            apiService = RetrofitClient.apiService,
            projectDao = projectDao,
            context = context
        )
    }
    
    // The ViewModel will observe this Flow - remains unchanged for UI compatibility
    val allProjects: Flow<List<Project>> = projectDao.getAllProjects()

    /**
     * Phase 3: Enhanced initialization with network sync
     * This function now tries to sync from API first, then falls back to local data
     */
    suspend fun seedDatabaseIfNeeded() {
        Log.d(TAG, "Initializing ProjectRepository...")
        
        try {
            // Step 1: Initialize repository
            networkRepository.initializeRepository()
            
            // Step 2: Try to sync fresh data from API
            val syncResult = networkRepository.syncProjectsFromApi()
            
            when (syncResult) {
                is NetworkResult.Success -> {
                    Log.d(TAG, "Successfully synced ${syncResult.data.size} projects from API")
                }
                is NetworkResult.Error -> {
                    Log.w(TAG, "API sync failed: ${syncResult.message}. Using cached data.")
                }
                is NetworkResult.Loading -> {
                    Log.d(TAG, "Sync in progress...")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error during repository initialization", e)
        }
    }
    
    /**
     * Phase 3: Force refresh data from API
     * Call this when user pulls to refresh or explicitly requests fresh data
     */
    suspend fun refreshProjects(): NetworkResult<List<Project>> {
        Log.d(TAG, "Force refreshing projects...")
        return networkRepository.forceRefreshProjects()
    }
    
    /**
     * Phase 3: Get cached projects count
     */
    suspend fun getCachedProjectsCount(): Int {
        return networkRepository.getCachedProjectsCount()
    }
    
    /**
     * Phase 3: Check if we have cached data
     */
    suspend fun hasCachedData(): Boolean {
        return networkRepository.hasCachedData()
    }
    
    /**
     * Phase 3: Get a specific project by ID
     */
    suspend fun getProjectById(projectId: String): Project? {
        return networkRepository.getProjectById(projectId)
    }
}
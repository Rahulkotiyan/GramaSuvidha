package com.example.gramasuvidha.network

import android.content.Context
import android.util.Log
import com.example.gramasuvidha.models.Notice
import com.example.gramasuvidha.models.Project
import com.example.gramasuvidha.models.ProjectDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import retrofit2.Response

/**
 * Phase 3: Network Repository
 * Handles all network operations and syncs data with the local Room database
 * Implements the offline-first architecture required by the SRS
 */
class NetworkRepository(
    private val apiService: ApiService,
    private val projectDao: ProjectDao,
    private val context: Context
) {
    
    companion object {
        private const val TAG = "NetworkRepository"
    }
    
    /**
     * Get all projects from the local database (offline-first)
     * The UI observes this Flow, which automatically updates when data changes
     */
    val allProjects: Flow<List<Project>> = projectDao.getAllProjects()
    
    /**
     * Sync projects from the API to the local database
     * This is the core of the offline-first sync logic
     * Flow: API -> Room Database -> UI (via Flow)
     */
    suspend fun syncProjectsFromApi(): NetworkResult<List<Project>> {
        return try {
            Log.d(TAG, "Starting project sync from API...")
            Log.d(TAG, "API URL: ${RetrofitClient.BASE_URL}projects?select=*")
            
            // Step 1: Fetch data from real API
            val result = safeApiCall { apiService.getAllProjects() }
            
            when (result) {
                is NetworkResult.Success -> {
                    val projects = result.data
                    Log.d(TAG, "API Response Success: Successfully fetched ${projects.size} projects from API")
                    Log.d(TAG, "First project data: ${if (projects.isNotEmpty()) projects.first() else "No projects"}")
                    
                    // Step 2: Save to Room database
                    projectDao.insertAll(projects)
                    Log.d(TAG, "Successfully saved projects to local database")
                    
                    // Step 3: Return the synced data
                    NetworkResult.success(projects)
                }
                is NetworkResult.Error -> {
                    Log.e(TAG, "API Error: ${result.message}, Code: ${result.code}", null)
                    Log.e(TAG, "API Error Details: Full error response", null)
                    
                    // Fall back to cached data if API fails
                    val cachedProjects = projectDao.getAllProjects().first()
                    if (cachedProjects.isNotEmpty()) {
                        Log.d(TAG, "Using cached data (${cachedProjects.size} projects)")
                        NetworkResult.success(cachedProjects)
                    } else {
                        NetworkResult.error(result.message, result.code)
                    }
                }
                is NetworkResult.Loading -> {
                    Log.d(TAG, "API Response: Loading state")
                    NetworkResult.loading()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error during sync: ${e.message}", e)
            NetworkResult.error("Failed to sync and no cached data available: ${e.message}", null)
        }
    }
    
    /**
     * Force refresh data from API
     * This can be called when user pulls to refresh
     */
    suspend fun forceRefreshProjects(): NetworkResult<List<Project>> {
        Log.d(TAG, "Force refreshing projects from API...")
        return syncProjectsFromApi()
    }
    
    /**
     * Get cached projects count for debugging
     */
    suspend fun getCachedProjectsCount(): Int {
        return projectDao.getAllProjects().first().size
    }
    
    /**
     * Check if we have cached data
     */
    suspend fun hasCachedData(): Boolean {
        return getCachedProjectsCount() > 0
    }
    
    /**
     * Get a specific project by ID (from local cache)
     */
    suspend fun getProjectById(projectId: String): Project? {
        return projectDao.getAllProjects().first()
            .find { it.project_id == projectId }
    }
    
    /**
     * Initialize the repository
     * Checks if cached data exists
     */
    suspend fun initializeRepository() {
        try {
            val currentProjects = projectDao.getAllProjects().first()
            
            if (currentProjects.isEmpty()) {
                Log.d(TAG, "No cached data found, will sync from API when needed")
                // Test API connectivity
                testApiConnection()
            } else {
                Log.d(TAG, "Found ${currentProjects.size} cached projects")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing repository", e)
        }
    }
    
    /**
     * Fetch all notices from the API
     */
    suspend fun getAllNotices(): NetworkResult<List<Notice>> {
        return try {
            Log.d(TAG, "Fetching notices from API...")
            val result = safeApiCall { apiService.getAllNotices() }
            
            when (result) {
                is NetworkResult.Success -> {
                    Log.d(TAG, "Successfully fetched ${result.data.size} notices")
                    NetworkResult.success(result.data)
                }
                is NetworkResult.Error -> {
                    Log.e(TAG, "Notice API Error: ${result.message}")
                    NetworkResult.error(result.message, result.code)
                }
                is NetworkResult.Loading -> NetworkResult.loading()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching notices: ${e.message}", e)
            NetworkResult.error("Failed to fetch notices: ${e.message}", null)
        }
    }

    /**
     * Test API connectivity to debug issues
     */
    private suspend fun testApiConnection() {
        try {
            Log.d(TAG, "Testing API connectivity...")
            val testResult = safeApiCall { apiService.testConnection() }
            
            when (testResult) {
                is NetworkResult.Success -> {
                    Log.d(TAG, "API Connection Test SUCCESS: Found ${testResult.data.size} projects")
                }
                is NetworkResult.Error -> {
                    Log.e(TAG, "API Connection Test FAILED: ${testResult.message}, Code: ${testResult.code}")
                }
                is NetworkResult.Loading -> {
                    Log.d(TAG, "API Connection Test: Loading state")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "API Connection Test Exception: ${e.message}", e)
        }
    }
}

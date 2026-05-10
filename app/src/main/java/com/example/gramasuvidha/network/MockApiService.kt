package com.example.gramasuvidha.network

import android.content.Context
import com.example.gramasuvidha.models.Project
import com.example.gramasuvidha.models.ProjectResponse
import com.example.gramasuvidha.utils.loadProjectsFromAsset
import kotlinx.coroutines.delay

/**
 * Phase 3: Mock API Service for Testing
 * Simulates network responses when the real backend is not available
 * This helps test the offline-first architecture during development
 */
class MockApiService(private val context: Context) {
    
    companion object {
        private const val TAG = "MockApiService"
        private const val SIMULATED_DELAY = 2000L // 2 seconds to simulate network latency
    }
    
    /**
     * Mock implementation of getAllProjects
     * Simulates network delay and returns mock data
     */
    suspend fun getAllProjects(): NetworkResult<ProjectResponse> {
        return try {
            // Simulate network delay
            delay(SIMULATED_DELAY)
            
            // Load mock data from assets
            val projects = loadProjectsFromAsset(context)
            val response = ProjectResponse(projects)
            
            NetworkResult.success(response)
        } catch (e: Exception) {
            NetworkResult.error("Mock API error: ${e.message}", null)
        }
    }
    
    /**
     * Mock implementation of getProjectById
     */
    suspend fun getProjectById(projectId: String): NetworkResult<Project> {
        return try {
            delay(SIMULATED_DELAY / 2) // Shorter delay for single item
            
            val projects = loadProjectsFromAsset(context)
            val project = projects.find { it.project_id == projectId }
            
            if (project != null) {
                NetworkResult.success(project)
            } else {
                NetworkResult.error("Project not found: $projectId", 404)
            }
        } catch (e: Exception) {
            NetworkResult.error("Mock API error: ${e.message}", null)
        }
    }
    
    /**
     * Simulate network error for testing error handling
     */
    suspend fun simulateNetworkError(): NetworkResult<ProjectResponse> {
        delay(SIMULATED_DELAY)
        return NetworkResult.error("Simulated network error", 500)
    }
    
    /**
     * Simulate slow network for testing loading states
     */
    suspend fun simulateSlowNetwork(): NetworkResult<ProjectResponse> {
        delay(SIMULATED_DELAY * 3) // 6 seconds delay
        val projects = loadProjectsFromAsset(context)
        val response = ProjectResponse(projects)
        return NetworkResult.success(response)
    }
}

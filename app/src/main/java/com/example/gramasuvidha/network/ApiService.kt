package com.example.gramasuvidha.network

import com.example.gramasuvidha.models.Project
import com.example.gramasuvidha.models.ProjectResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Phase 3: API Service Interface
 * Defines the REST API endpoints for fetching project data from the backend
 */
interface ApiService {
    
    /**
     * Fetch all projects from the backend
     * @return Response containing list of projects
     */
    @GET("projects?select=*")
    suspend fun getAllProjects(): Response<List<Project>>
    
    /**
     * Test endpoint to verify API connectivity
     */
    @GET("projects?select=project_id&limit=1")
    suspend fun testConnection(): Response<List<Project>>
    
    /**
     * Fetch a specific project by ID
     * @param projectId The unique identifier for the project
     * @return Response containing a single Project
     */
    @GET("projects/{projectId}")
    suspend fun getProjectById(@Path("projectId") projectId: String): Response<Project>
    
    /**
     * Fetch projects with optional query parameters for filtering
     * This can be used for future enhancements like filtering by progress, etc.
     */
    @GET("projects")
    suspend fun getProjects(
        // Future parameters can be added here
        // @Query("status") status: String? = null,
        // @Query("limit") limit: Int? = null
    ): Response<List<Project>>
}

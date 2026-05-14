package com.example.gramasuvidha.network

import com.example.gramasuvidha.models.Feedback
import com.example.gramasuvidha.models.FeedbackRequest
import com.example.gramasuvidha.models.FeedbackResponse
import com.example.gramasuvidha.models.Notice
import com.example.gramasuvidha.models.Project
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Phase 3: API Service Interface Defines the REST API endpoints for fetching project data from the
 * backend
 */
interface ApiService {

    /**
     * Fetch all projects from the backend
     * @return Response containing list of projects
     */
    @GET("projects?select=*") suspend fun getAllProjects(): Response<List<Project>>

    /** Test endpoint to verify API connectivity */
    @GET("projects?select=project_id&limit=1") suspend fun testConnection(): Response<List<Project>>

    /**
     * Fetch a specific project by ID
     * @param projectId The unique identifier for the project
     * @return Response containing a single Project
     */
    @GET("projects/{projectId}")
    suspend fun getProjectById(@Path("projectId") projectId: String): Response<Project>

    /**
     * Fetch projects with optional query parameters for filtering This can be used for future
     * enhancements like filtering by progress, etc.
     */
    @GET("projects")
    suspend fun getProjects(
    // Future parameters can be added here
    // @Query("status") status: String? = null,
    // @Query("limit") limit: Int? = null
    ): Response<List<Project>>

    // Phase 4: Feedback API Endpoints

    /** Submit feedback for a project */
    @POST("feedback")
    suspend fun submitFeedback(@Body feedbackRequest: FeedbackRequest): Response<FeedbackResponse>

    /** Get all feedback for a specific project */
    @GET("feedback?select=*&project_id=eq.{projectId}")
    suspend fun getFeedbackForProject(
            @Path("projectId") projectId: String
    ): Response<List<Feedback>>

    /** Get all feedback (admin only) */
    @GET("feedback?select=*") suspend fun getAllFeedback(): Response<List<Feedback>>

    /** Fetch all notices from the backend */
    @GET("notices?select=*&order=date.desc") suspend fun getAllNotices(): Response<List<Notice>>

    /** Update feedback status (admin only) */
    @POST("rpc/update_feedback_status")
    suspend fun updateFeedbackStatus(
            @Body updateRequest: Map<String, String>
    ): Response<Map<String, Any>>
}

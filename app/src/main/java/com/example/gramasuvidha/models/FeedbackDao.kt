package com.example.gramasuvidha.models

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Phase 4: Feedback Data Access Object
 * Handles database operations for feedback
 */
@Dao
interface FeedbackDao {
    
    /**
     * Insert a new feedback entry
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeedback(feedback: Feedback)
    
    /**
     * Get all feedback for a specific project
     */
    @Query("SELECT * FROM feedback_table WHERE project_id = :projectId ORDER BY timestamp DESC")
    fun getFeedbackForProject(projectId: String): Flow<List<Feedback>>
    
    /**
     * Get all feedback (for admin purposes)
     */
    @Query("SELECT * FROM feedback_table ORDER BY timestamp DESC")
    fun getAllFeedback(): Flow<List<Feedback>>
    
    /**
     * Get feedback by status
     */
    @Query("SELECT * FROM feedback_table WHERE status = :status ORDER BY timestamp DESC")
    fun getFeedbackByStatus(status: String): Flow<List<Feedback>>
    
    /**
     * Update feedback status
     */
    @Query("UPDATE feedback_table SET status = :status WHERE feedback_id = :feedbackId")
    suspend fun updateFeedbackStatus(feedbackId: String, status: String)
    
    /**
     * Delete feedback
     */
    @Delete
    suspend fun deleteFeedback(feedback: Feedback)
    
    /**
     * Get average rating for a project
     */
    @Query("SELECT AVG(rating) FROM feedback_table WHERE project_id = :projectId")
    suspend fun getAverageRatingForProject(projectId: String): Float?
    
    /**
     * Get rating counts for a project
     */
    @Query("SELECT rating, COUNT(*) as count FROM feedback_table WHERE project_id = :projectId GROUP BY rating ORDER BY rating")
    suspend fun getRatingDistributionForProject(projectId: String): List<RatingCount>
    
    /**
     * Get feedback statistics
     */
    @Query("SELECT COUNT(*) as total_feedback, AVG(rating) as average_rating FROM feedback_table")
    suspend fun getFeedbackStats(): FeedbackStats?
}

/**
 * Helper class for rating distribution queries
 */
data class RatingCount(
    val rating: Int,
    val count: Int
)

/**
 * Helper class for feedback statistics
 */
data class FeedbackStats(
    val total_feedback: Int,
    val average_rating: Float?
)

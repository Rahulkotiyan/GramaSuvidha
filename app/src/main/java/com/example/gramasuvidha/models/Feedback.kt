package com.example.gramasuvidha.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Phase 4: Feedback Model
 * Represents user feedback and ratings for projects
 */
@Entity(tableName = "feedback_table")
data class Feedback(
    @PrimaryKey
    @SerializedName("feedback_id")
    val feedback_id: String,
    
    @SerializedName("project_id")
    val project_id: String,
    
    @SerializedName("user_id")
    val user_id: String, // In real app, this would be authenticated user ID
    
    @SerializedName("rating")
    val rating: Int, // 1-5 star rating
    
    @SerializedName("category")
    val category: String, // e.g., "Infrastructure", "Water Supply", etc.
    
    @SerializedName("feedback_text")
    val feedback_text: String,
    
    @SerializedName("timestamp")
    val timestamp: String, // ISO 8601 format
    
    @SerializedName("status")
    val status: String = "pending", // "pending", "reviewed", "resolved"
    
    @SerializedName("is_anonymous")
    val is_anonymous: Boolean = false
)

/**
 * Response wrapper for feedback operations
 */
data class FeedbackResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: Feedback? = null
)

/**
 * Request body for submitting feedback
 */
data class FeedbackRequest(
    @SerializedName("project_id")
    val project_id: String,
    @SerializedName("rating")
    val rating: Int,
    @SerializedName("category")
    val category: String,
    @SerializedName("feedback_text")
    val feedback_text: String,
    @SerializedName("is_anonymous")
    val is_anonymous: Boolean = false
)

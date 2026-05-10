package com.example.gramasuvidha.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

// We don't need to save the top-level response to the database, just the list items
data class ProjectResponse(val projects: List<Project>)

@Entity(tableName = "projects_table") // Tells Room to make a table out of this class
data class Project(
    @PrimaryKey // Tells Room this is the unique ID for each row
    @SerializedName("project_id")
    val project_id: String,
    @SerializedName("title_en")
    val title_en: String,
    @SerializedName("title_kn")
    val title_kn: String,
    @SerializedName("description_en")
    val description_en: String,
    @SerializedName("description_kn")
    val description_kn: String,
    @SerializedName("budget")
    val budget: String,
    @SerializedName("progress_percentage")
    val progress_percentage: Int,

    // Handle flat image fields from Supabase
    @SerializedName("before_url")
    val before_url: String,
    @SerializedName("current_url")
    val current_url: String
)


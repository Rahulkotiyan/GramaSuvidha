package com.example.gramasuvidha.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

// We don't need to save the top-level response to the database, just the list items
data class ProjectResponse(val projects: List<Project>)

@Entity(tableName = "projects_table") // Tells Room to make a table out of this class
data class Project(
    @PrimaryKey // Tells Room this is the unique ID for each row
    val project_id: String,
    val title_en: String,
    val title_kn: String,
    val description_en: String,
    val description_kn: String,
    val budget: String,
    val progress_percentage: Int,

    @Embedded // Room cannot store nested objects natively, so @Embedded flattens this into the same table
    val images: ProjectImages
)

data class ProjectImages(
    val before_url: String,
    val current_url: String
)
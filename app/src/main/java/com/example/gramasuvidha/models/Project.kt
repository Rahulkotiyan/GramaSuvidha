package com.example.gramasuvidha.models

data class ProjectResponse(val projects: List<Project>)

data class Project(
    val project_id: String,
    val title_en: String,
    val title_kn: String,
    val description_en: String,
    val description_kn: String,
    val budget: String,
    val progress_percentage: Int,
    val images: ProjectImages
)

data class ProjectImages(
    val before_url: String,
    val current_url: String
)
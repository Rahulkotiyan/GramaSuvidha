package com.example.gramasuvidha.data

import android.content.Context
import com.example.gramasuvidha.models.Project
import com.example.gramasuvidha.models.ProjectDao
import com.example.gramasuvidha.utils.loadProjectsFromAsset
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class ProjectRepository(private val projectDao: ProjectDao, private val context: Context) {

    // The ViewModel will observe this Flow
    val allProjects: Flow<List<Project>> = projectDao.getAllProjects()

    // This function seeds the Room database with your JSON file on first launch
    suspend fun seedDatabaseIfNeeded() {
        // Look at the first emission from the database
        val currentProjects = projectDao.getAllProjects().first()

        if (currentProjects.isEmpty()) {
            // Database is empty! Read from JSON and insert into Room
            val mockData = loadProjectsFromAsset(context)
            projectDao.insertAll(mockData)
        }
    }
}
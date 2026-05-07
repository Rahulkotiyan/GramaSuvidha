package com.example.gramasuvidha.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gramasuvidha.models.Project
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {

    // Fetch all projects. Returning a Flow means the UI will automatically
    // update whenever the database changes!
    @Query("SELECT * FROM projects_table")
    fun getAllProjects(): Flow<List<Project>>

    // Insert a list of projects. If a project already exists with the same ID, replace it.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(projects: List<Project>)
}
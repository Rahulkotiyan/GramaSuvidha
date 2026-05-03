package com.example.gramasuvidha.utils

import android.content.Context
import com.google.gson.Gson
import java.io.InputStreamReader

// Import your data classes from the models package
import com.example.gramasuvidha.models.Project
import com.example.gramasuvidha.models.ProjectResponse

fun loadProjectsFromAsset(context: Context): List<Project> {
    val inputStream = context.assets.open("mock_projects.json")
    val reader = InputStreamReader(inputStream)
    val response = Gson().fromJson(reader, ProjectResponse::class.java)
    reader.close()
    return response.projects
}
package com.example.gramasuvidha.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.gramasuvidha.models.Project
import com.example.gramasuvidha.utils.loadProjectsFromAsset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProjectViewModel(application: Application) : AndroidViewModel(application) {

    private val _projects = MutableStateFlow<List<Project>>(emptyList())
    val projects: StateFlow<List<Project>> = _projects

    init {
        // Load mock data on startup
        _projects.value = loadProjectsFromAsset(application)
    }
}
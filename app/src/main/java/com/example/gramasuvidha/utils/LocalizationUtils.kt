package com.example.gramasuvidha.utils

import android.content.Context
import com.example.gramasuvidha.models.Project

// Helper function to get the correct text based on language preference
fun getLocalizedTitle(context: Context, project: Project): String {
    return if (LocaleManager.isKannada(context)) {
        project.title_kn
    } else {
        project.title_en
    }
}

fun getLocalizedDescription(context: Context, project: Project): String {
    return if (LocaleManager.isKannada(context)) {
        project.description_kn
    } else {
        project.description_en
    }
}

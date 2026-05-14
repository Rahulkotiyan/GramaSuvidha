package com.example.gramasuvidha // Make sure this matches your actual package name!

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.gramasuvidha.ui.screens.AuthenticatedMainAppScreen
import com.example.gramasuvidha.ui.theme.GramaSuvidhaTheme
import com.example.gramasuvidha.utils.LocaleManager
import com.example.gramasuvidha.viewmodels.ProjectViewModel
import com.example.gramasuvidha.work.WorkManagerScheduler

class MainActivity : ComponentActivity() {
    // Instantiate ViewModel at the Activity level
    private val viewModel: ProjectViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("GramaSuvidha", "MainActivity: onCreate started")

        try {
            // Apply saved language locale
            val savedLanguage = LocaleManager.getLanguage(this)
            Log.d("GramaSuvidha", "MainActivity: Applying locale: $savedLanguage")
            LocaleManager.applyLocale(this, savedLanguage)
        } catch (e: Exception) {
            Log.e("GramaSuvidha", "MainActivity: Error applying locale", e)
        }

        try {
            // Phase 3: Initialize WorkManager for background sync
            Log.d("GramaSuvidha", "MainActivity: Initializing WorkManager...")
            WorkManagerScheduler.schedulePeriodicSync(this)
            Log.d("GramaSuvidha", "MainActivity: WorkManager initialized successfully")
        } catch (e: Exception) {
            Log.e("GramaSuvidha", "MainActivity: Error initializing WorkManager", e)
        }

        try {
            setContent {
                GramaSuvidhaTheme {
                    Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                    ) { AuthenticatedMainAppScreen(viewModel = viewModel) }
                }
            }
            Log.d("GramaSuvidha", "MainActivity: setContent completed successfully")
        } catch (e: Exception) {
            Log.e("GramaSuvidha", "MainActivity: Error in setContent", e)
            throw e
        }
    }
}

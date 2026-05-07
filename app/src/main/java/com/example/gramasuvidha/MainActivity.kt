package com.example.gramasuvidha // Make sure this matches your actual package name!

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.gramasuvidha.ui.screens.MainAppScreen
import com.example.gramasuvidha.ui.theme.GramaSuvidhaTheme
import com.example.gramasuvidha.utils.LocaleManager
import com.example.gramasuvidha.viewmodels.ProjectViewModel

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
            setContent {
                GramaSuvidhaTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        MainAppScreen(viewModel = viewModel)
                    }
                }
            }
            Log.d("GramaSuvidha", "MainActivity: setContent completed successfully")
        } catch (e: Exception) {
            Log.e("GramaSuvidha", "MainActivity: Error in setContent", e)
            throw e
        }
    }
}
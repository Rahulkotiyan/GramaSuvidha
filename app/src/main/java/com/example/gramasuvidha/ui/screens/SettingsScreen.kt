package com.example.gramasuvidha.ui.screens

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.gramasuvidha.R
import com.example.gramasuvidha.utils.LocaleManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    android.util.Log.d("GramaSuvidha", "SettingsScreen: Rendering")
    val context = LocalContext.current
    val activity = context as? Activity
    var isKannada by remember { mutableStateOf(LocaleManager.isKannada(context)) }

    Scaffold(topBar = { TopAppBar(title = { Text(stringResource(R.string.settings_title)) }) }) {
        padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.language_label),
                    style = MaterialTheme.typography.bodyLarge
                )
                Switch(
                    checked = isKannada,
                    onCheckedChange = { newValue ->
                        isKannada = newValue
                        val languageCode = if (newValue) "kn" else "en"
                        LocaleManager.setLanguage(context, languageCode, activity)
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                stringResource(R.string.data_sync_label),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { /* TODO: Trigger manual Room DB sync */}) {
                Text(stringResource(R.string.sync_button))
            }
        }
    }
}

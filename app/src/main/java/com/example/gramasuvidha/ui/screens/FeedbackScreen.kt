package com.example.gramasuvidha.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.gramasuvidha.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen() {
    android.util.Log.d("GramaSuvidha", "FeedbackScreen: Rendering")
    var feedbackText by remember { mutableStateOf("") }

    Scaffold(topBar = { TopAppBar(title = { Text(stringResource(R.string.feedback_title)) }) }) {
        padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text(
                stringResource(R.string.report_issue_title),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = feedbackText,
                onValueChange = { feedbackText = it },
                label = { Text(stringResource(R.string.feedback_hint)) },
                modifier = Modifier.fillMaxWidth().height(150.dp),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* TODO: Save feedback in Room DB or send to API */},
                modifier = Modifier.fillMaxWidth()
            ) { Text(stringResource(R.string.submit_report_button)) }
        }
    }
}

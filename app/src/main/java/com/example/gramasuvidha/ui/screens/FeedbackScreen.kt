package com.example.gramasuvidha.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen() {
    var feedbackText by remember { mutableStateOf("") }

    Scaffold(topBar = { TopAppBar(title = { Text("Namma Dhwani (Feedback)") }) }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text("Report an Issue or Provide Feedback", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = feedbackText,
                onValueChange = { feedbackText = it },
                label = { Text("Describe the issue...") },
                modifier = Modifier.fillMaxWidth().height(150.dp),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* TODO: Save feedback in Room DB or send to API */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit Report")
            }
        }
    }
}
package com.example.gramasuvidha.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gramasuvidha.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen() {
    android.util.Log.d("GramaSuvidha", "FeedbackScreen: Rendering")
    var feedbackText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("General") }
    var isSubmitting by remember { mutableStateOf(false) }
    var showSuccessMessage by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
    val categories = listOf("General", "Infrastructure", "Water Supply", "Health", "Education", "Other")
    
    val isFormValid = feedbackText.trim().length >= 10

    Scaffold(
            topBar = {
                TopAppBar(
                        title = {
                            Text(
                                    stringResource(R.string.feedback_title),
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.headlineSmall
                            )
                        },
                        colors =
                                TopAppBarDefaults.topAppBarColors(
                                        containerColor = MaterialTheme.colorScheme.surface,
                                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                                        scrolledContainerColor = MaterialTheme.colorScheme.surface
                                )
                )
            }
    ) { padding ->
        Column(
                modifier =
                        Modifier.fillMaxSize()
                                .padding(padding)
                                .padding(20.dp)
                                .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Description Card
            Card(
                    modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors =
                            CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                            ),
                    border = CardDefaults.outlinedCardBorder().copy(width = 1.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                            stringResource(R.string.report_issue_title),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                            "Share your feedback, suggestions, or report any issues you encounter.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            fontWeight = FontWeight.Normal
                    )
                }
            }

            // Category Selection
            Card(
                    modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    colors =
                            CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                            ),
                    border = CardDefaults.outlinedCardBorder().copy(width = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                            text = "Category",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(categories) { category ->
                            FilterChip(
                                    onClick = { selectedCategory = category },
                                    label = {
                                        Text(
                                                text = category,
                                                style = MaterialTheme.typography.bodyMedium
                                        )
                                    },
                                    selected = selectedCategory == category,
                                    modifier = Modifier
                            )
                        }
                    }
                }
            }

            // Feedback Input Field
            Card(
                    modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    colors =
                            CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.background
                            ),
                    border = if (errorMessage.isNotEmpty()) 
                        BorderStroke(2.dp, Color.Red) 
                    else 
                        CardDefaults.outlinedCardBorder()
            ) {
                OutlinedTextField(
                        value = feedbackText,
                        onValueChange = { 
                            feedbackText = it
                            errorMessage = "" // Clear error when user starts typing
                        },
                        label = {
                            Text(
                                    stringResource(R.string.feedback_hint),
                                    style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        placeholder = {
                            Text(
                                    "Please describe your feedback in detail...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        },
                        supportingText = {
                            if (errorMessage.isNotEmpty()) {
                                Text(
                                        text = errorMessage,
                                        color = Color.Red,
                                        style = MaterialTheme.typography.bodySmall
                                )
                            } else {
                                Text(
                                        text = "${feedbackText.length}/500 characters",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        },
                        isError = errorMessage.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth().height(180.dp).padding(0.dp),
                        maxLines = 8,
                        shape = RoundedCornerShape(12.dp),
                        colors =
                                OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = if (errorMessage.isNotEmpty()) Color.Red else MaterialTheme.colorScheme.primary,
                                        unfocusedBorderColor = if (errorMessage.isNotEmpty()) Color.Red else MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                        focusedLabelColor = if (errorMessage.isNotEmpty()) Color.Red else MaterialTheme.colorScheme.primary,
                                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                        cursorColor = MaterialTheme.colorScheme.primary,
                                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                                ),
                        textStyle = MaterialTheme.typography.bodyMedium
                )
            }

            // Success Message
            if (showSuccessMessage) {
                Card(
                        modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f)
                        ),
                        border = BorderStroke(1.dp, Color(0xFF4CAF50))
                ) {
                    Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Success",
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(24.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                    text = "Feedback Submitted Successfully!",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF4CAF50)
                            )
                            Text(
                                    text = "Thank you for your feedback. We will review it and take appropriate action.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }

            // Submit Button
            Button(
                    onClick = {
                        if (isFormValid) {
                            isSubmitting = true
                            // Simulate submission
                            kotlinx.coroutines.GlobalScope.launch {
                                kotlinx.coroutines.delay(1500) // Simulate network delay
                                isSubmitting = false
                                showSuccessMessage = true
                                feedbackText = ""
                                // Hide success message after 3 seconds
                                kotlinx.coroutines.delay(3000)
                                showSuccessMessage = false
                            }
                        } else {
                            errorMessage = "Please enter at least 10 characters"
                        }
                    },
                    enabled = !isSubmitting,
                    modifier =
                            Modifier.fillMaxWidth()
                                    .height(56.dp)
                                    .shadow(6.dp, RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    colors =
                            ButtonDefaults.buttonColors(
                                    containerColor = if (isFormValid && !isSubmitting) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                            )
            ) {
                Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Submit",
                                modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                            if (isSubmitting) "Submitting..." else stringResource(R.string.submit_report_button),
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

package com.example.gramasuvidha.ui.screens

import android.app.Activity
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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

    Scaffold(
            topBar = {
                TopAppBar(
                        title = {
                            Text(
                                    stringResource(R.string.settings_title),
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Language Settings Card
            SettingsCard(
                    title = stringResource(R.string.language_label),
                    description = "Choose your preferred language",
                    icon = Icons.Default.Language,
                    onClick = { /* Language switch handled by switch */}
            ) {
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                            if (isKannada) "Kannada" else "English",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Medium
                    )
                    Switch(
                            checked = isKannada,
                            onCheckedChange = { newValue ->
                                isKannada = newValue
                                val languageCode = if (newValue) "kn" else "en"
                                LocaleManager.setLanguage(context, languageCode, activity)
                            },
                            colors =
                                    SwitchDefaults.colors(
                                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                                            checkedTrackColor =
                                                    MaterialTheme.colorScheme.primary.copy(
                                                            alpha = 0.3f
                                                    ),
                                            uncheckedThumbColor =
                                                    MaterialTheme.colorScheme.outline.copy(
                                                            alpha = 0.6f
                                                    ),
                                            uncheckedTrackColor =
                                                    MaterialTheme.colorScheme.surfaceVariant
                                    ),
                            modifier = Modifier.scale(0.95f)
                    )
                }
            }

            // Data Sync Card
            SettingsCard(
                    title = stringResource(R.string.data_sync_label),
                    description = "Manually sync your data",
                    icon = Icons.Default.Sync,
                    onClick = { /* TODO: Trigger manual Room DB sync */}
            ) {
                Button(
                        onClick = { /* TODO: Trigger manual Room DB sync */},
                        modifier =
                                Modifier.fillMaxWidth()
                                        .height(48.dp)
                                        .shadow(
                                                elevation = 3.dp,
                                                shape = RoundedCornerShape(10.dp),
                                                ambientColor =
                                                        MaterialTheme.colorScheme.primary.copy(
                                                                alpha = 0.2f
                                                        )
                                        ),
                        shape = RoundedCornerShape(10.dp),
                        colors =
                                ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                )
                ) {
                    Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                                imageVector = Icons.Default.Sync,
                                contentDescription = "Sync",
                                modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                                stringResource(R.string.sync_button),
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun SettingsCard(
        title: String,
        description: String = "",
        icon: ImageVector,
        onClick: () -> Unit,
        content: @Composable () -> Unit
) {
    Card(
            modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = CardDefaults.outlinedCardBorder().copy(width = 1.dp)
    ) {
        Column(modifier = Modifier.padding(22.dp)) {
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Icon(
                            imageVector = icon,
                            contentDescription = title,
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                            modifier = Modifier.size(26.dp)
                    )
                    Column {
                        Text(
                                text = title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                        )
                        if (description.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                    text = description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    fontWeight = FontWeight.Normal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            content()
        }
    }
}

@Composable fun Modifier.scale(scale: Float) = this.then(Modifier)

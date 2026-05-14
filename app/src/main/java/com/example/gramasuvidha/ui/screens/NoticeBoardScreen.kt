package com.example.gramasuvidha.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gramasuvidha.R
import com.example.gramasuvidha.models.Notice
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gramasuvidha.viewmodels.NoticeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoticeBoardScreen(
    navController: NavController,
    noticeViewModel: NoticeViewModel = viewModel()
) {
    val notices by noticeViewModel.notices.collectAsState()
    val isLoading by noticeViewModel.isLoading.collectAsState()

    Scaffold(
                topBar = {
                        TopAppBar(
                                title = {
                                        Text(
                                                stringResource(R.string.notice_board_title),
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                style = MaterialTheme.typography.headlineSmall
                                        )
                                },
                                colors =
                                        TopAppBarDefaults.topAppBarColors(
                                                containerColor = MaterialTheme.colorScheme.surface,
                                                titleContentColor =
                                                        MaterialTheme.colorScheme.onSurface,
                                                scrolledContainerColor =
                                                        MaterialTheme.colorScheme.surface
                                        )
                        )
                }
        ) { padding ->
                PullToRefreshBox(
                        isRefreshing = isLoading,
                        onRefresh = { noticeViewModel.fetchNotices() },
                        modifier = Modifier.fillMaxSize().padding(padding)
                ) {
                        LazyColumn(
                                modifier = Modifier.fillMaxSize().padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(bottom = 8.dp)
                        ) {
                                items(notices) { notice -> NoticeCard(notice = notice) }
                        }
                }
        }
}

@Composable
fun NoticeCard(notice: Notice) {
        val context = LocalContext.current
        val isKannada = context.resources.configuration.locales[0].language == "kn"
        val title = if (isKannada) notice.title_kn else notice.title_en
        val content = if (isKannada) notice.content_kn else notice.content_en

        val priorityColor =
                when (notice.priority) {
                        "high" -> Color.Red.copy(alpha = 0.8f)
                        "medium" -> Color(0xFFFF9800).copy(alpha = 0.8f) // Orange
                        else -> Color(0xFF4CAF50).copy(alpha = 0.8f) // Green
                }

        Card(
                modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors =
                        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder().copy(width = 1.dp)
        ) {
                Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                        ) {
                                Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                                text = title,
                                                style = MaterialTheme.typography.titleLarge,
                                                fontWeight = FontWeight.SemiBold,
                                                color = MaterialTheme.colorScheme.onSurface,
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                                text = content,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color =
                                                        MaterialTheme.colorScheme.onSurface.copy(
                                                                alpha = 0.8f
                                                        ),
                                                lineHeight = 20.sp
                                        )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(
                                        horizontalAlignment = Alignment.End,
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                        Box(
                                                modifier =
                                                        Modifier.background(
                                                                        color =
                                                                                priorityColor.copy(
                                                                                        alpha = 0.2f
                                                                                ),
                                                                        shape =
                                                                                RoundedCornerShape(
                                                                                        8.dp
                                                                                )
                                                                )
                                                                .padding(
                                                                        horizontal = 8.dp,
                                                                        vertical = 4.dp
                                                                )
                                        ) {
                                                Text(
                                                        text = notice.priority.uppercase(),
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = priorityColor,
                                                        fontWeight = FontWeight.Bold
                                                )
                                        }
                                        Text(
                                                text = notice.date,
                                                style = MaterialTheme.typography.bodySmall,
                                                color =
                                                        MaterialTheme.colorScheme.onSurface.copy(
                                                                alpha = 0.6f
                                                        )
                                        )
                                }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                                Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                        Icon(
                                                imageVector = Icons.Default.Label,
                                                contentDescription = "Category",
                                                tint =
                                                        MaterialTheme.colorScheme.primary.copy(
                                                                alpha = 0.7f
                                                        ),
                                                modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                                text = notice.category,
                                                style = MaterialTheme.typography.bodySmall,
                                                color =
                                                        MaterialTheme.colorScheme.primary.copy(
                                                                alpha = 0.8f
                                                        ),
                                                fontWeight = FontWeight.Medium
                                        )
                                }
                        }
                }
        }
}

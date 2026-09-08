package com.toolbox.videodownloader.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toolbox.videodownloader.ui.theme.AppColors

enum class DownloadStatus { DOWNLOADING, COMPLETED, FAILED, QUEUED }

data class DownloadItem(
    val name: String,
    val meta: String,       // "1080p • 156 MB"
    val progress: Float,    // 0f..1f
    val speed: String,      // "1.2 MB/s"
    val thumbnailBg: Color,
    val status: DownloadStatus,
)

val sampleDownloads = listOf(
    DownloadItem("Beautiful Nature - 4K...", "1080p • 156 MB", 0.45f, "1.2 MB/s", Color(0xFF2E5A6E), DownloadStatus.DOWNLOADING),
    DownloadItem("Music - Chill Vibes.mp3", "320 kbps • 12 MB", 0.78f, "0.8 MB/s", Color(0xFF6D3E91), DownloadStatus.DOWNLOADING),
    DownloadItem("Funny Cats Compilation", "720p • 98 MB", 0f, "Waiting...", Color(0xFF8A6D3E), DownloadStatus.QUEUED),
    DownloadItem("Dubai Skyline 4K", "4K • 520 MB", 0f, "Queued...", Color(0xFF3E5C8A), DownloadStatus.QUEUED),
)

@Composable
fun DownloadsScreen(
    downloadingCount: Int = 2,
    completedCount: Int = 5,
    failedCount: Int = 1,
    items: List<DownloadItem> = sampleDownloads,
    onNavigate: (String) -> Unit,
) {
    var selectedTab by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(0) }
    val tabs = listOf("Downloading  $downloadingCount", "Completed  $completedCount", "Failed  $failedCount")

    Scaffold(
        containerColor = AppColors.Background,
        topBar = {
            Text(
                "Downloads",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary,
                modifier = Modifier.padding(start = 20.dp, top = 18.dp, bottom = 8.dp)
            )
        },
        bottomBar = { BottomNavBar(selected = "Downloads", onNavigate = onNavigate) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = AppColors.Background,
                contentColor = AppColors.Primary,
                indicator = { positions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(positions[selectedTab]),
                        color = AppColors.Primary
                    )
                }
            ) {
                tabs.forEachIndexed { index, label ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(label, fontSize = 12.sp) },
                        selectedContentColor = AppColors.Primary,
                        unselectedContentColor = AppColors.TextSecondary
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                items.forEach { item -> DownloadRow(item) }
            }
        }
    }
}

@Composable
private fun DownloadRow(item: DownloadItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(AppColors.Surface)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(52.dp).clip(RoundedCornerShape(10.dp)).background(item.thumbnailBg))
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(item.name, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = AppColors.TextPrimary, maxLines = 1)
            Text(item.meta, fontSize = 11.sp, color = AppColors.TextSecondary)
            Spacer(Modifier.height(6.dp))
            if (item.status == DownloadStatus.DOWNLOADING) {
                LinearProgressIndicator(
                    progress = { item.progress },
                    modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
                    color = AppColors.Primary,
                    trackColor = AppColors.Divider,
                )
                Spacer(Modifier.height(4.dp))
                Text("${(item.progress * 100).toInt()}%   •   ${item.speed}", fontSize = 10.sp, color = AppColors.TextMuted)
            } else {
                Text(item.speed, fontSize = 10.sp, color = AppColors.TextMuted)
            }
        }
        Spacer(Modifier.width(8.dp))
        val icon = when (item.status) {
            DownloadStatus.DOWNLOADING -> Icons.Default.Pause
            DownloadStatus.COMPLETED -> Icons.Default.CheckCircle
            DownloadStatus.FAILED -> Icons.Default.Error
            DownloadStatus.QUEUED -> Icons.Default.Download
        }
        val tint = when (item.status) {
            DownloadStatus.COMPLETED -> AppColors.Success
            DownloadStatus.FAILED -> AppColors.Danger
            else -> AppColors.TextSecondary
        }
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(22.dp))
    }
}

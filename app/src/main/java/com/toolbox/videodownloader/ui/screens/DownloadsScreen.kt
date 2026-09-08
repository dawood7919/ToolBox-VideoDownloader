package com.toolbox.videodownloader.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toolbox.videodownloader.ToolBoxViewModel
import com.toolbox.videodownloader.model.DownloadItem as RealDownload
import com.toolbox.videodownloader.model.DownloadStatus as RealStatus
import coil.compose.AsyncImage
import com.toolbox.videodownloader.ui.theme.AppColors
import java.util.Locale

@Composable
fun DownloadsScreen(
    onNavigate: (String) -> Unit,
) {
    val viewModel: ToolBoxViewModel = viewModel()
    val downloads by viewModel.downloads.collectAsStateWithLifecycle()

    val active = downloads.filter { it.isActive }
    val completed = downloads.filter { it.status == RealStatus.Completed }
    val failed = downloads.filter { it.status == RealStatus.Failed }

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf(
        "Downloading  ${active.size}",
        "Completed  ${completed.size}",
        "Failed  ${failed.size}",
    )
    val visible = when (selectedTab) {
        0 -> active
        1 -> completed
        else -> failed
    }.sortedByDescending { it.createdAt }

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
            if (visible.isEmpty()) {
                Box(Modifier.fillMaxSize().padding(bottom = 120.dp), contentAlignment = Alignment.Center) {
                    Text(
                        "Nothing here yet — paste a link on Home\nand start a download.",
                        fontSize = 12.sp,
                        color = AppColors.TextMuted,
                        lineHeight = 18.sp
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.padding(horizontal = 20.dp)) {
                    items(visible, key = { it.id }) { item ->
                        DownloadRow(
                            item = item,
                            onPause = { viewModel.pause(item.id) },
                            onResume = { viewModel.resume(item.id) },
                            onRetry = { viewModel.retry(item.id) },
                            onCancel = { viewModel.cancel(item.id) },
                            onRemove = { viewModel.removeCompleted(item.id) },
                        )
                    }
                }
            }
        }
    }
}

private fun statusLabel(item: RealDownload): String = when (item.status) {
    RealStatus.Queued -> "Queued"
    RealStatus.Resolving -> "Preparing"
    RealStatus.Running -> "Downloading"
    RealStatus.Paused -> "Paused"
    RealStatus.Completed -> "Saved"
    RealStatus.Failed -> "Failed"
}

private fun metaLine(item: RealDownload): String = buildString {
    if (item.totalBytes > 0) {
        append(formatSize(item.totalBytes))
    } else {
        append("size unknown")
    }
    item.errorMessage?.let { append(" · $it") }
}

private fun formatSize(bytes: Long): String = when {
    bytes <= 0 -> "0 B"
    bytes < 1024 -> "$bytes B"
    bytes < 1024 * 1024 -> String.format(Locale.US, "%.0f KB", bytes / 1024.0)
    bytes < 1024L * 1024 * 1024 -> String.format(Locale.US, "%.1f MB", bytes / (1024.0 * 1024))
    else -> String.format(Locale.US, "%.2f GB", bytes / (1024.0 * 1024 * 1024))
}

private fun formatSpeed(bytesPerSecond: Long): String = when {
    bytesPerSecond <= 0 -> "—"
    bytesPerSecond < 1024 * 1024 -> String.format(Locale.US, "%.1f KB/s", bytesPerSecond / 1024.0)
    else -> String.format(Locale.US, "%.1f MB/s", bytesPerSecond / (1024.0 * 1024))
}

@Composable
private fun DownloadRow(
    item: RealDownload,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onRetry: () -> Unit,
    onCancel: () -> Unit,
    onRemove: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(AppColors.Surface)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(AppColors.SurfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            if (item.thumbnailUrl != null && !item.mimeType.startsWith("audio")) {
                AsyncImage(
                    model = item.thumbnailUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    if (item.mimeType.startsWith("audio")) Icons.Default.MusicNote else Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = AppColors.Primary,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(item.title, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = AppColors.TextPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(metaLine(item), fontSize = 11.sp, color = AppColors.TextSecondary, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(6.dp))
            if (item.status == RealStatus.Running) {
                item.progress?.let { progress ->
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
                        color = AppColors.Primary,
                        trackColor = AppColors.Divider,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "${(progress * 100).toInt()}%   •   ${formatSpeed(item.speedBytesPerSecond)}",
                        fontSize = 10.sp,
                        color = AppColors.TextMuted
                    )
                } ?: Text(statusLabel(item), fontSize = 10.sp, color = AppColors.TextMuted)
            } else {
                Text(statusLabel(item), fontSize = 10.sp, color = AppColors.TextMuted)
            }
        }
        Spacer(Modifier.width(8.dp))
        when (item.status) {
            RealStatus.Running, RealStatus.Queued, RealStatus.Resolving ->
                IconButton(onClick = onPause, modifier = Modifier.size(34.dp)) {
                    Icon(Icons.Default.Pause, contentDescription = "Pause", tint = AppColors.TextSecondary, modifier = Modifier.size(20.dp))
                }
            RealStatus.Paused ->
                IconButton(onClick = onResume, modifier = Modifier.size(34.dp)) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Resume", tint = AppColors.Primary, modifier = Modifier.size(20.dp))
                }
            RealStatus.Failed ->
                IconButton(onClick = onRetry, modifier = Modifier.size(34.dp)) {
                    Icon(Icons.Default.Refresh, contentDescription = "Retry", tint = AppColors.Danger, modifier = Modifier.size(20.dp))
                }
            RealStatus.Completed ->
                IconButton(onClick = onRemove, modifier = Modifier.size(34.dp)) {
                    Icon(Icons.Default.Close, contentDescription = "Remove", tint = AppColors.Success, modifier = Modifier.size(20.dp))
                }
        }
    }
}

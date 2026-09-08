package com.toolbox.videodownloader.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
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

data class PlaylistVideo(
    val index: Int,
    val title: String,
    val meta: String,     // "12:34 • 1080p • 156 MB"
    val thumbnailBg: Color,
    val selected: Boolean,
)

val samplePlaylist = listOf(
    PlaylistVideo(1, "Beautiful Nature - 4K", "12:34 • 1080p • 156 MB", Color(0xFF2E5A6E), true),
    PlaylistVideo(2, "Amazing Places", "10:21 • 1080p • 124 MB", Color(0xFF3E5C8A), true),
    PlaylistVideo(3, "Ocean Waves", "08:45 • 720p • 98 MB", Color(0xFF2E6E5E), false),
    PlaylistVideo(4, "City Life", "09:12 • 1080p • 110 MB", Color(0xFF6D6D3E), false),
    PlaylistVideo(5, "Sunset Timelapse", "06:48 • 720p • 76 MB", Color(0xFF8A5C3E), false),
)

@Composable
fun PlaylistScreen(
    videos: List<PlaylistVideo> = samplePlaylist,
    onToggleSelect: (Int) -> Unit,
    onSelectAll: () -> Unit,
    selectedCount: Int,
    selectedTotalSize: String,
    onDownloadSelected: () -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        containerColor = AppColors.Background,
        topBar = {
            BackTopBar(
                title = "Playlist (${videos.size} videos)",
                onBack = onBack,
                trailingText = "Select All",
                onTrailingClick = onSelectAll
            )
        },
        bottomBar = {
            Column(modifier = Modifier.background(AppColors.Background).padding(20.dp)) {
                Button(
                    onClick = onDownloadSelected,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.Primary)
                ) {
                    Icon(Icons.Default.Download, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Download Selected ($selectedCount)", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                }
                Spacer(Modifier.height(4.dp))
                Text("~$selectedTotalSize", fontSize = 11.sp, color = AppColors.TextSecondary)
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp)) {
            videos.forEach { video ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(AppColors.Surface)
                        .clickable { onToggleSelect(video.index) }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("${video.index}.", fontSize = 12.sp, color = AppColors.TextMuted, modifier = Modifier.width(20.dp))
                    Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(10.dp)).background(video.thumbnailBg))
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(video.title, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = AppColors.TextPrimary, maxLines = 1)
                        Text(video.meta, fontSize = 11.sp, color = AppColors.TextSecondary)
                    }
                    Checkbox(
                        checked = video.selected,
                        onCheckedChange = { onToggleSelect(video.index) },
                        colors = CheckboxDefaults.colors(checkedColor = AppColors.Primary)
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

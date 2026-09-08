package com.toolbox.videodownloader.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toolbox.videodownloader.ui.theme.AppColors

enum class MediaType { VIDEO, AUDIO, PLAYLIST }

data class LibraryItem(
    val name: String,
    val size: String,
    val meta: String,   // "1080p" أو "MP3" أو تاريخ
    val date: String,   // "Today" / "Yesterday" / "3 days ago"
    val thumbnailBg: Color,
    val type: MediaType,
)

val sampleLibrary = listOf(
    LibraryItem("Beautiful Nature - 4K.mp4", "156 MB", "1080p", "Today", Color(0xFF2E5A6E), MediaType.VIDEO),
    LibraryItem("Music - Chill Vibes.mp3", "12 MB", "MP3", "Today", Color(0xFF6D3E91), MediaType.AUDIO),
    LibraryItem("Funny Cats Compilation.mp4", "98 MB", "720p", "Yesterday", Color(0xFF8A6D3E), MediaType.VIDEO),
    LibraryItem("Dubai Skyline 4K.mp4", "520 MB", "4K", "2 days ago", Color(0xFF3E5C8A), MediaType.VIDEO),
    LibraryItem("Playlist (12 videos).mp4", "1.2 GB", "1080p", "3 days ago", Color(0xFF4E4E7A), MediaType.PLAYLIST),
)

@Composable
fun LibraryScreen(
    items: List<LibraryItem> = sampleLibrary,
    onBack: () -> Unit,
    onItemClick: (LibraryItem) -> Unit,
) {
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Video", "Audio", "Playlists")
    val filtered = when (selectedFilter) {
        "Video" -> items.filter { it.type == MediaType.VIDEO }
        "Audio" -> items.filter { it.type == MediaType.AUDIO }
        "Playlists" -> items.filter { it.type == MediaType.PLAYLIST }
        else -> items
    }

    Scaffold(containerColor = AppColors.Background, topBar = {
        BackTopBar(title = "Downloads", onBack = onBack)
    }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                filters.forEach { filter ->
                    val selected = filter == selectedFilter
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (selected) AppColors.Primary else AppColors.Surface)
                            .clickable { selectedFilter = filter }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(filter, fontSize = 12.sp, color = if (selected) Color.White else AppColors.TextSecondary)
                    }
                }
            }
            Spacer(Modifier.height(14.dp))
            filtered.forEach { item -> LibraryRow(item, onClick = { onItemClick(item) }) }
        }
    }
}

@Composable
private fun LibraryRow(item: LibraryItem, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(AppColors.Surface)
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(52.dp).clip(RoundedCornerShape(10.dp)).background(item.thumbnailBg))
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(item.name, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = AppColors.TextPrimary, maxLines = 1)
            Text("${item.size}  •  ${item.meta}  •  ${item.date}", fontSize = 11.sp, color = AppColors.TextSecondary)
        }
        if (item.type == MediaType.AUDIO || item.type == MediaType.PLAYLIST) {
            Icon(Icons.Default.MoreVert, contentDescription = null, tint = AppColors.TextSecondary)
        } else {
            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = AppColors.Primary)
        }
    }
}

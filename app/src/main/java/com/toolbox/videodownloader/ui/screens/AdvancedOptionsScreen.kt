package com.toolbox.videodownloader.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toolbox.videodownloader.ui.theme.AppColors

@Composable
fun AdvancedOptionsScreen(
    playlistEnabled: Boolean,
    onPlaylistToggle: (Boolean) -> Unit,
    downloadMultipleEnabled: Boolean,
    onDownloadMultipleToggle: (Boolean) -> Unit,
    convertToMp4: Boolean,
    onConvertToggle: (Boolean) -> Unit,
    saveFolderPath: String,
    fileNameMode: String,
    onApply: () -> Unit,
    onBack: () -> Unit,
    onOpenPlaylist: () -> Unit,
) {
    Scaffold(containerColor = AppColors.Background, topBar = {
        BackTopBar(title = "Advanced Options", onBack = onBack)
    }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp)) {
            Spacer(Modifier.height(6.dp))

            ToggleOptionRow(
                icon = Icons.Default.PlaylistPlay,
                title = "Playlist",
                subtitle = "Download entire playlist",
                checked = playlistEnabled,
                onCheckedChange = onPlaylistToggle
            )
            if (playlistEnabled) {
                NavRow(icon = Icons.Default.QueueMusic, title = "My Playlist (12 videos)", onClick = onOpenPlaylist)
            }

            ToggleOptionRow(
                icon = Icons.Default.LibraryAdd,
                title = "Download Multiple",
                subtitle = "Add to download queue",
                checked = downloadMultipleEnabled,
                onCheckedChange = onDownloadMultipleToggle
            )

            NavRow(icon = Icons.Default.Folder, title = "Save to Folder", subtitle = saveFolderPath, onClick = {})
            NavRow(icon = Icons.Default.DriveFileRenameOutline, title = "File Name", subtitle = fileNameMode, onClick = {})

            ToggleOptionRow(
                icon = Icons.Default.SwapHoriz,
                title = "Convert to MP4",
                subtitle = "(if available)",
                checked = convertToMp4,
                onCheckedChange = onConvertToggle
            )

            Spacer(Modifier.weight(1f))
            Button(
                onClick = onApply,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.Primary)
            ) {
                Text("Apply", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ToggleOptionRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(AppColors.Surface)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RowIcon(icon)
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = AppColors.TextPrimary)
            Text(subtitle, fontSize = 11.sp, color = AppColors.TextSecondary)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = AppColors.Primary)
        )
    }
}

@Composable
private fun NavRow(icon: ImageVector, title: String, subtitle: String? = null, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(AppColors.Surface)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RowIcon(icon)
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = AppColors.TextPrimary)
            if (subtitle != null) Text(subtitle, fontSize = 11.sp, color = AppColors.TextSecondary)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = AppColors.TextMuted)
    }
}

@Composable
private fun RowIcon(icon: ImageVector) {
    Box(
        modifier = Modifier.size(36.dp).clip(CircleShape).background(AppColors.Primary.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = AppColors.Primary, modifier = Modifier.size(18.dp))
    }
}

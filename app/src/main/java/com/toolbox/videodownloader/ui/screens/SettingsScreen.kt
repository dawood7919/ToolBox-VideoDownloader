package com.toolbox.videodownloader.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toolbox.videodownloader.ui.theme.AppColors

@Composable
fun SettingsScreen(
    defaultQuality: String,
    defaultFormat: String,
    defaultAudioQuality: String,
    downloadFolder: String,
    notificationsEnabled: Boolean,
    onNotificationsToggle: (Boolean) -> Unit,
    autoDownloadEnabled: Boolean,
    onAutoDownloadToggle: (Boolean) -> Unit,
    cacheSize: String,
    onClearCache: () -> Unit,
    onAbout: () -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(containerColor = AppColors.Background, topBar = {
        BackTopBar(title = "Settings", onBack = onBack)
    }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp)) {
            Spacer(Modifier.height(4.dp))
            SettingsNavRow(Icons.Default.HighQuality, "Default Quality", defaultQuality) {}
            SettingsNavRow(Icons.Default.VideoFile, "Default Format", defaultFormat) {}
            SettingsNavRow(Icons.Default.GraphicEq, "Default Audio Quality", defaultAudioQuality) {}
            SettingsNavRow(Icons.Default.Folder, "Download Folder", downloadFolder) {}

            Spacer(Modifier.height(12.dp))
            SettingsToggleRow(Icons.Default.Notifications, "Notifications", notificationsEnabled, onNotificationsToggle)
            SettingsToggleRow(Icons.Default.PlaylistAddCheck, "Auto Download (from playlist)", autoDownloadEnabled, onAutoDownloadToggle)

            Spacer(Modifier.height(12.dp))
            SettingsNavRow(Icons.Default.CleaningServices, "Clear Cache", cacheSize, onClick = onClearCache)
            SettingsNavRow(Icons.Default.Info, "About", null, onClick = onAbout)

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SettingsNavRow(icon: ImageVector, title: String, value: String?, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(AppColors.Surface)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = AppColors.Primary, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(14.dp))
        Text(title, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = AppColors.TextPrimary, modifier = Modifier.weight(1f))
        if (value != null) {
            Text(value, fontSize = 12.sp, color = AppColors.TextSecondary)
            Spacer(Modifier.width(6.dp))
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = AppColors.TextMuted)
    }
}

@Composable
private fun SettingsToggleRow(icon: ImageVector, title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(AppColors.Surface)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = AppColors.Primary, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(14.dp))
        Text(title, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = AppColors.TextPrimary, modifier = Modifier.weight(1f))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = androidx.compose.ui.graphics.Color.White, checkedTrackColor = AppColors.Primary)
        )
    }
}

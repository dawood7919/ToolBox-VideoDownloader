package com.toolbox.videodownloader.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toolbox.videodownloader.ui.theme.AppColors

@Composable
fun PlayerScreen(
    videoInfo: DetectedVideoInfo,
    isPlaying: Boolean,
    onPlayPauseToggle: () -> Unit,
    currentTime: String = "02:38",
    progress: Float = 0.21f,
    onBack: () -> Unit,
) {
    Scaffold(containerColor = AppColors.Background, topBar = {
        BackTopBar(title = videoInfo.title, onBack = onBack)
    }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp)) {
            // شاشة الفيديو
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 10f)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Brush.verticalGradient(listOf(videoInfo.thumbnailBg, videoInfo.thumbnailBg.copy(alpha = 0.5f)))),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier.size(56.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = null, tint = Color.White, modifier = Modifier.size(30.dp)
                    )
                }
            }

            Spacer(Modifier.height(10.dp))
            // شريط التقدم
            Slider(
                value = progress,
                onValueChange = {},
                colors = SliderDefaults.colors(
                    thumbColor = AppColors.Primary,
                    activeTrackColor = AppColors.Primary,
                    inactiveTrackColor = AppColors.Divider
                )
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(currentTime, fontSize = 11.sp, color = AppColors.TextSecondary)
                Text(videoInfo.duration, fontSize = 11.sp, color = AppColors.TextSecondary)
            }

            Spacer(Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.VolumeUp, contentDescription = null, tint = AppColors.TextSecondary)
                Icon(
                    if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = AppColors.TextPrimary,
                    modifier = Modifier.size(30.dp)
                )
                Icon(Icons.Default.PhotoCamera, contentDescription = null, tint = AppColors.TextSecondary)
                Text("1.0x", fontSize = 12.sp, color = AppColors.TextSecondary, fontWeight = FontWeight.Medium)
                Icon(Icons.Default.Fullscreen, contentDescription = null, tint = AppColors.TextSecondary)
            }

            Spacer(Modifier.height(22.dp))
            Text("Video Info", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = AppColors.TextPrimary)
            Spacer(Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(AppColors.Surface)
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                InfoRow("Resolution", "1080p")
                InfoRow("Format", "MP4")
                InfoRow("Size", videoInfo.sizeApprox)
                InfoRow("Duration", videoInfo.duration)
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

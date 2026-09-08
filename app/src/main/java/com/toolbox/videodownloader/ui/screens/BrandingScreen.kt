package com.toolbox.videodownloader.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toolbox.videodownloader.ui.theme.AppColors

data class FeatureTile(val icon: ImageVector, val label1: String, val label2: String)

private val featureTiles = listOf(
    FeatureTile(Icons.Default.Devices, "Multiple", "Platforms"),
    FeatureTile(Icons.Default.HighQuality, "High Quality", "Download"),
    FeatureTile(Icons.Default.MusicNote, "Audio", "Only"),
    FeatureTile(Icons.Default.PlaylistPlay, "Playlist", "Support"),
    FeatureTile(Icons.Default.CloudDownload, "Background", "Download"),
    FeatureTile(Icons.Default.ThumbUp, "Easy to", "Use"),
)

@Composable
fun BrandingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(AppColors.Background, Color(0xFF10192C)))
            )
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(84.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(Brush.verticalGradient(listOf(AppColors.Primary, AppColors.PrimaryDark))),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Download, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
        }
        Spacer(Modifier.height(18.dp))
        Text("Video Downloader", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = AppColors.TextPrimary)
        Spacer(Modifier.height(6.dp))
        Text("Fast  •  Simple  •  Powerful", fontSize = 12.sp, color = AppColors.TextSecondary)

        Spacer(Modifier.height(32.dp))
        // شبكة المميزات 2x3
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            featureTiles.chunked(3).forEach { rowTiles ->
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    rowTiles.forEach { tile -> FeatureTileView(tile) }
                }
            }
        }

        Spacer(Modifier.height(36.dp))
        Text("Your favorite videos", fontSize = 12.sp, color = AppColors.TextSecondary)
        Text("always with you", fontSize = 12.sp, color = AppColors.TextSecondary)
        Spacer(Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Brush.horizontalGradient(listOf(AppColors.Primary, Color(0xFFE94AA0))))
        )
    }
}

@Composable
private fun FeatureTileView(tile: FeatureTile) {
    Column(
        modifier = Modifier
            .size(96.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(AppColors.Surface)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(tile.icon, contentDescription = null, tint = AppColors.Primary, modifier = Modifier.size(22.dp))
        Spacer(Modifier.height(6.dp))
        Text(tile.label1, fontSize = 10.sp, color = AppColors.TextPrimary, fontWeight = FontWeight.Medium)
        Text(tile.label2, fontSize = 10.sp, color = AppColors.TextPrimary, fontWeight = FontWeight.Medium)
    }
}

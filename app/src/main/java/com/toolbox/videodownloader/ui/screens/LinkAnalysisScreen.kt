package com.toolbox.videodownloader.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
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

data class DetectedVideoInfo(
    val title: String = "Beautiful Nature - 4K Scenic Video",
    val platform: String = "YouTube",
    val duration: String = "12:34",
    val videoId: String = "dQw4w9WgXcQ",
    val quality: String = "1080p (Full HD)",
    val sizeApprox: String = "156 MB",
    val thumbnailBg: Color = Color(0xFF2E5A6E),
)

@Composable
fun LinkAnalysisScreen(
    linkText: String,
    onLinkChange: (String) -> Unit,
    isAnalyzed: Boolean,
    videoInfo: DetectedVideoInfo,
    onAnalyzeClick: () -> Unit,
    onContinueClick: () -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(containerColor = AppColors.Background, topBar = {
        BackTopBar(title = "Video Downloader", onBack = onBack)
    }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(8.dp))
            // حقل الرابط
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(AppColors.SurfaceVariant)
                    .border(1.dp, AppColors.Divider, RoundedCornerShape(14.dp))
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    BasicTextField(
                        value = linkText,
                        onValueChange = onLinkChange,
                        singleLine = true,
                        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp, color = AppColors.TextPrimary),
                        cursorBrush = androidx.compose.ui.graphics.SolidColor(AppColors.Primary),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Spacer(Modifier.height(14.dp))
            Button(
                onClick = onAnalyzeClick,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.Primary)
            ) {
                Text("Analyze", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            }

            if (isAnalyzed) {
                Spacer(Modifier.height(20.dp))
                // معاينة الفيديو
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Brush.verticalGradient(listOf(videoInfo.thumbnailBg, videoInfo.thumbnailBg.copy(alpha = 0.6f)))),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier.size(52.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
                    }
                }
                Spacer(Modifier.height(10.dp))
                Text(videoInfo.title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = AppColors.TextPrimary)
                Text("${videoInfo.platform} • ${videoInfo.duration}", fontSize = 12.sp, color = AppColors.TextSecondary)

                Spacer(Modifier.height(18.dp))
                Text("Detected Information", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = AppColors.TextPrimary)
                Spacer(Modifier.height(8.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(AppColors.Surface)
                        .border(1.dp, AppColors.Divider, RoundedCornerShape(14.dp))
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    InfoRow("Platform", videoInfo.platform)
                    InfoRow("Duration", videoInfo.duration)
                    InfoRow("Video ID", videoInfo.videoId)
                    InfoRow("Video Quality", videoInfo.quality)
                    InfoRow("Size (approx.)", videoInfo.sizeApprox)
                }

                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = onContinueClick,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.Primary)
                ) {
                    Text("Continue", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

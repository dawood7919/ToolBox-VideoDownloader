package com.toolbox.videodownloader.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toolbox.videodownloader.ui.theme.AppColors

// ==================== نموذج بيانات المنصة المدعومة ====================
data class Platform(val name: String, val icon: ImageVector, val color: Color)

private val supportedPlatforms = listOf(
    Platform("YouTube", Icons.Default.PlayArrow, Color(0xFFFF0000)),
    Platform("Facebook", Icons.Default.Facebook, Color(0xFF1877F2)),
    Platform("Instagram", Icons.Default.CameraAlt, Color(0xFFE1306C)),
    Platform("TikTok", Icons.Default.MusicNote, Color(0xFF000000)),
    Platform("X (Twitter)", Icons.Default.Close, Color(0xFF000000)),
    Platform("Vimeo", Icons.Default.Videocam, Color(0xFF1AB7EA)),
    Platform("Dailymotion", Icons.Default.OndemandVideo, Color(0xFF00AAFF)),
    Platform("And more...", Icons.Default.MoreHoriz, AppColors.TextSecondary),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    linkText: String,
    onLinkChange: (String) -> Unit,
    onDownloadClick: () -> Unit,
    onNavigate: (String) -> Unit,
) {
    Scaffold(
        containerColor = AppColors.Background,
        topBar = { HomeTopBar() },
        bottomBar = { BottomNavBar(selected = "Home", onNavigate = onNavigate) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(24.dp))
            DownloaderCard(
                linkText = linkText,
                onLinkChange = onLinkChange,
                onDownloadClick = onDownloadClick
            )
            Spacer(Modifier.height(28.dp))
            Text(
                text = "Supported Platforms",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppColors.TextPrimary
            )
            Spacer(Modifier.height(14.dp))
            PlatformsGrid()
            Spacer(Modifier.height(16.dp))
        }
    }
}

// ==================== الشريط العلوي ====================
@Composable
private fun HomeTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColors.Background)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(AppColors.Primary),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.GridView, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(10.dp))
        Column {
            Text("ToolBox", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = AppColors.TextPrimary)
            Text("All Tools in One Place", fontSize = 11.sp, color = AppColors.TextSecondary)
        }
    }
}

// ==================== كارت الداونلودر (الأيقونة + الحقل + الزرار) ====================
@Composable
private fun DownloaderCard(
    linkText: String,
    onLinkChange: (String) -> Unit,
    onDownloadClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(AppColors.Surface)
            .border(1.dp, AppColors.Divider, RoundedCornerShape(20.dp))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(
                    Brush.verticalGradient(listOf(AppColors.Primary, AppColors.PrimaryDark))
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Download, contentDescription = null, tint = Color.White, modifier = Modifier.size(30.dp))
        }
        Spacer(Modifier.height(14.dp))
        Text("Video Downloader", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = AppColors.TextPrimary)
        Spacer(Modifier.height(4.dp))
        Text(
            "Download videos from multiple platforms\nin high quality",
            fontSize = 12.sp,
            color = AppColors.TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 16.sp
        )
        Spacer(Modifier.height(20.dp))

        // حقل إدخال الرابط
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
            Icon(Icons.Default.Link, contentDescription = null, tint = AppColors.TextMuted, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(10.dp))
            Box(modifier = Modifier.weight(1f)) {
                if (linkText.isEmpty()) {
                    Text("Paste video link here...", fontSize = 13.sp, color = AppColors.TextMuted)
                }
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

        // زرار التحميل
        Button(
            onClick = onDownloadClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AppColors.Primary)
        ) {
            Text("Download", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
        }
    }
}

// ==================== شبكة المنصات المدعومة ====================
@Composable
private fun PlatformsGrid() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.height(180.dp)
    ) {
        items(supportedPlatforms) { platform ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(AppColors.Surface)
                        .border(1.dp, AppColors.Divider, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(platform.icon, contentDescription = platform.name, tint = platform.color, modifier = Modifier.size(22.dp))
                }
                Spacer(Modifier.height(6.dp))
                Text(platform.name, fontSize = 10.sp, color = AppColors.TextSecondary, textAlign = TextAlign.Center)
            }
        }
    }
}



package com.toolbox.videodownloader.ui.screens

import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.toolbox.videodownloader.ui.theme.AppColors
import java.util.Locale

/** Plays a media URL while it is still online — before anything is downloaded. */
@Composable
fun PlayerScreen(
    videoInfo: DetectedVideoInfo,
    streamUrl: String?,
    thumbnailUrl: String?,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val player = remember(streamUrl) {
        streamUrl?.let {
            ExoPlayer.Builder(context).build().apply {
                setMediaItem(MediaItem.fromUri(it))
                prepare()
                playWhenReady = true
            }
        }
    }
    DisposableEffect(player) { onDispose { player?.release() } }

    var isPlaying by remember { mutableStateOf(true) }
    LaunchedEffect(player) {
        while (true) {
            kotlinx.coroutines.delay(500)
            player?.let { isPlaying = it.isPlaying }
        }
    }

    Scaffold(containerColor = AppColors.Background, topBar = {
        BackTopBar(title = videoInfo.title, onBack = onBack)
    }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            // شاشة الفيديو
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 10f)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Brush.verticalGradient(listOf(videoInfo.thumbnailBg, videoInfo.thumbnailBg.copy(alpha = 0.5f)))),
                contentAlignment = Alignment.Center
            ) {
                if (player != null) {
                    AndroidView(
                        factory = { ctx ->
                            PlayerView(ctx).apply {
                                this.player = player
                                useController = true
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // مفيش لينك حقيقي — اعرض البوستر
                    AsyncImage(
                        model = thumbnailUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier.size(56.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(30.dp))
                    }
                    Text(
                        "Nothing to play yet — analyze a link first",
                        fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.align(Alignment.BottomCenter).padding(10.dp)
                    )
                }
            }

            Spacer(Modifier.height(10.dp))
            if (player != null) {
                Text(
                    if (isPlaying) "Streaming online…" else "Paused",
                    fontSize = 11.sp, color = AppColors.TextSecondary
                )
            }

            Spacer(Modifier.height(18.dp))
            // البوستر
            if (thumbnailUrl != null) {
                Text("Thumbnail", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = AppColors.TextPrimary)
                Spacer(Modifier.height(8.dp))
                AsyncImage(
                    model = thumbnailUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .clip(RoundedCornerShape(14.dp))
                )
                Spacer(Modifier.height(18.dp))
            }

            Text("Video Info", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = AppColors.TextPrimary)
            Spacer(Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(AppColors.Surface)
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                InfoRow("Title", videoInfo.title)
                InfoRow("Source", videoInfo.platform)
                InfoRow("Size", videoInfo.sizeApprox)
                InfoRow("Duration", videoInfo.duration)
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

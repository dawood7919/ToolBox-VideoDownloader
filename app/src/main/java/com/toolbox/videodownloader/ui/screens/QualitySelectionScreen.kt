package com.toolbox.videodownloader.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toolbox.videodownloader.ui.theme.AppColors

data class QualityOption(val label: String, val sizeApprox: String, val isBest: Boolean = false)

val videoQualityOptions = listOf(
    QualityOption("4K (2160p)", "~520 MB", isBest = true),
    QualityOption("2K (1440p)", "~320 MB"),
    QualityOption("1080p (Full HD)", "~156 MB"),
    QualityOption("720p (HD)", "~98 MB"),
    QualityOption("480p", "~54 MB"),
    QualityOption("360p", "~32 MB"),
)

@Composable
fun QualitySelectionScreen(
    videoInfo: DetectedVideoInfo,
    selectedQuality: String,
    onQualitySelected: (String) -> Unit,
    audioOnlySelected: Boolean,
    onAudioOnlyToggle: (Boolean) -> Unit,
    onStartDownload: () -> Unit,
    onBack: () -> Unit,
    onOpenAdvanced: () -> Unit = {},
) {
    Scaffold(
        containerColor = AppColors.Background,
        topBar = { BackTopBar(title = "Download Options", onBack = onBack) },
        bottomBar = {
            // الزر دايمًا ظاهر تحت — مش جوه السكرول
            StartDownloadButton(onClick = onStartDownload, enabled = true)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            // صف معلومات الفيديو
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(AppColors.Surface)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(52.dp).clip(RoundedCornerShape(10.dp)).background(videoInfo.thumbnailBg))
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(videoInfo.title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = AppColors.TextPrimary, maxLines = 1)
                    Text("${videoInfo.platform}  •  ${videoInfo.duration}", fontSize = 11.sp, color = AppColors.TextSecondary)
                }
            }

            Spacer(Modifier.height(18.dp))
            Text("Video Quality", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = AppColors.TextPrimary)
            Spacer(Modifier.height(8.dp))
            videoQualityOptions.forEachIndexed { index, option ->
                val selected = !audioOnlySelected && selectedQuality == option.label
                QualityRow(
                    option = option,
                    selected = selected,
                    // دخول متتالي ناعم لكل صف
                    enterDelayMs = index * 45,
                    onClick = {
                        onAudioOnlyToggle(false)
                        onQualitySelected(option.label)
                    }
                )
            }

            Spacer(Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
                Icon(Icons.Default.MusicNote, contentDescription = null, tint = AppColors.TextSecondary, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("Audio Only", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = AppColors.TextPrimary)
            }
            QualityRow(
                option = QualityOption("MP3 (320 kbps)", "~12 MB"),
                selected = audioOnlySelected,
                enterDelayMs = videoQualityOptions.size * 45,
                onClick = { onAudioOnlyToggle(true) }
            )

            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(AppColors.Surface)
                    .clickable { onOpenAdvanced() }
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Advanced Options", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = AppColors.TextPrimary)
                Icon(Icons.Default.ExpandMore, contentDescription = null, tint = AppColors.TextSecondary)
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun StartDownloadButton(onClick: () -> Unit, enabled: Boolean) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.96f else 1f, spring(stiffness = 400f), label = "press")

    // نبض خفيف على حافة الزر وهو جاهز
    val pulse = rememberInfiniteTransition(label = "pulse")
    val glow by pulse.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1400), RepeatMode.Reverse), label = "glow"
    )

    Button(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interaction,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .height(54.dp)
            .scale(scale),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            AppColors.Primary,
                            AppColors.Primary.copy(alpha = 0.75f + 0.25f * glow)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Download, contentDescription = null, tint = Color.White,
                    modifier = Modifier.size(19.dp).graphicsLayer {
                        // ايقونة بينزل ويرجع بلامسة خفيفة
                        translationY = (1f - glow) * 3f
                    }
                )
                Spacer(Modifier.width(8.dp))
                Text("Start Download", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            }
        }
    }
}

@Composable
private fun QualityRow(
    option: QualityOption,
    selected: Boolean,
    onClick: () -> Unit,
    enterDelayMs: Int = 0,
) {
    val bgColor by animateColorAsState(
        if (selected) AppColors.Primary.copy(alpha = 0.12f) else AppColors.Surface,
        tween(220), label = "bg"
    )
    val borderColor by animateColorAsState(
        if (selected) AppColors.Primary else AppColors.Divider,
        tween(220), label = "border"
    )
    val scale by animateFloatAsState(
        if (selected) 1.015f else 1f,
        spring(dampingRatio = 0.55f, stiffness = 380f), label = "scale"
    )
    // دخول متتالي: صف ورا صف
    val appear = remember { androidx.compose.animation.core.Animatable(0f) }
    androidx.compose.runtime.LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(enterDelayMs.toLong())
        appear.animateTo(1f, spring(stiffness = 120f, dampingRatio = 0.85f))
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .graphicsLayer {
                alpha = appear.value
                translationY = (1f - appear.value) * 40f
            }
            .scale(scale)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = AppColors.Primary, unselectedColor = AppColors.TextMuted)
        )
        Spacer(Modifier.width(6.dp))
        Text(option.label, fontSize = 13.sp, color = AppColors.TextPrimary, modifier = Modifier.weight(1f))
        Text(option.sizeApprox, fontSize = 11.sp, color = AppColors.TextSecondary)
        if (option.isBest) {
            Spacer(Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(AppColors.Primary)
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text("Best Quality", fontSize = 9.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

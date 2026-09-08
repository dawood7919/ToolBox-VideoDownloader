package com.toolbox.videodownloader.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ==================== الألوان الأساسية ====================
object AppColors {
    val Background = Color(0xFF0B1220)      // خلفية الشاشة الأساسية (كحلي غامق جدًا)
    val Surface = Color(0xFF141C2E)         // خلفية الكروت / الحقول
    val SurfaceVariant = Color(0xFF1B2438)  // خلفية عناصر أفتح شوية (زي القائمة السفلية)
    val Primary = Color(0xFF3B82F6)         // الأزرق الأساسي (الأزرار، الأيقونات المفعّلة)
    val PrimaryDark = Color(0xFF2563EB)
    val TextPrimary = Color(0xFFF1F5F9)     // نص أبيض تقريبًا
    val TextSecondary = Color(0xFF8B95A7)   // نص رمادي فاتح (وصف/تفاصيل)
    val TextMuted = Color(0xFF5B6478)       // نص باهت أكتر (تسميات صغيرة)
    val Divider = Color(0xFF232D42)
    val Success = Color(0xFF22C55E)
    val Warning = Color(0xFFF59E0B)
    val Danger = Color(0xFFEF4444)
    val IconInactive = Color(0xFF6B7488)
}

private val DarkColors = darkColorScheme(
    background = AppColors.Background,
    surface = AppColors.Surface,
    primary = AppColors.Primary,
    onBackground = AppColors.TextPrimary,
    onSurface = AppColors.TextPrimary,
    onPrimary = Color.White,
)

@Composable
fun ToolBoxTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColors,
        typography = MaterialTheme.typography,
        content = content
    )
}

// ==================== أنماط النصوص المستخدمة بكثرة ====================
object AppTextStyles {
    val ScreenTitle = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.SemiBold, color = AppColors.TextPrimary)
    val SectionTitle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = AppColors.TextPrimary)
    val ItemTitle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium, color = AppColors.TextPrimary)
    val ItemSubtitle = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Normal, color = AppColors.TextSecondary)
    val Caption = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Normal, color = AppColors.TextMuted)
}

package com.toolbox.videodownloader.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toolbox.videodownloader.ui.theme.AppColors

// ==================== شريط علوي فيه سهم رجوع + عنوان + زرار اختياري على اليمين ====================
@Composable
fun BackTopBar(
    title: String,
    onBack: () -> Unit,
    trailingText: String? = null,
    onTrailingClick: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColors.Background)
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = "رجوع", tint = AppColors.TextPrimary)
        }
        Text(
            title,
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
            color = AppColors.TextPrimary,
            modifier = Modifier.weight(1f)
        )
        if (trailingText != null) {
            TextButton(onClick = { onTrailingClick?.invoke() }) {
                Text(trailingText, color = AppColors.Primary, fontSize = 13.sp)
            }
        }
    }
}

// ==================== القائمة السفلية (مشتركة بين الشاشات الرئيسية) ====================
data class NavItem(val key: String, val label: String, val icon: ImageVector)

val navItems = listOf(
    NavItem("Home", "Home", Icons.Default.Home),
    NavItem("Tools", "Tools", Icons.Default.Apps),
    NavItem("Downloads", "Downloads", Icons.Default.Download),
    NavItem("Settings", "Settings", Icons.Default.Settings),
)

@Composable
fun BottomNavBar(selected: String, onNavigate: (String) -> Unit) {
    NavigationBar(containerColor = AppColors.Surface, tonalElevation = 0.dp) {
        navItems.forEach { item ->
            val isSelected = item.key == selected
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.key) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label, fontSize = 10.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AppColors.Primary,
                    selectedTextColor = AppColors.Primary,
                    unselectedIconColor = AppColors.IconInactive,
                    unselectedTextColor = AppColors.IconInactive,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

// ==================== صف معلومة بسيط (عنوان يسار + قيمة يمين) يستخدم في أكتر من شاشة ====================
@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 12.sp, color = AppColors.TextSecondary)
        Text(value, fontSize = 12.sp, color = AppColors.TextPrimary, fontWeight = FontWeight.Medium)
    }
}

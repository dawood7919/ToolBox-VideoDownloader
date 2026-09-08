package com.toolbox.videodownloader.ui

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.toolbox.videodownloader.ResolveUiState
import com.toolbox.videodownloader.ToolBoxViewModel
import com.toolbox.videodownloader.ui.screens.toAnalyzeUi
import com.toolbox.videodownloader.ui.screens.*

private object Routes {
    const val HOME = "home"
    const val LINK_ANALYSIS = "link_analysis"
    const val QUALITY = "quality"
    const val ADVANCED = "advanced"
    const val DOWNLOADS = "downloads"
    const val PLAYER = "player"
    const val PLAYLIST = "playlist"
    const val LIBRARY = "library"
    const val SETTINGS = "settings"
    const val BRANDING = "branding"
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val viewModel: ToolBoxViewModel = viewModel()
    val resolveState = viewModel.resolveState
    val downloads by viewModel.downloads.collectAsState()

    // ==================== حالة مشتركة بين الشاشات ====================
    val videoInfo = remember { DetectedVideoInfo() }

    var selectedQuality by remember { mutableStateOf("1080p (Full HD)") }
    var audioOnly by remember { mutableStateOf(false) }

    var playlistEnabled by remember { mutableStateOf(false) }
    var downloadMultiple by remember { mutableStateOf(false) }
    var convertToMp4 by remember { mutableStateOf(false) }

    var isPlaying by remember { mutableStateOf(false) }

    var notificationsEnabled by remember { mutableStateOf(true) }
    var autoDownloadEnabled by remember { mutableStateOf(false) }

    val playlistVideos = remember { mutableStateListOf(*samplePlaylist.toTypedArray()) }

    // The analyzed candidates drive the quality screen: real streams with
    // their real sizes when the extractor could list them, the mock fallback
    // when a plain page offered a single file with no quality ladder.
    val candidates = (resolveState as? ResolveUiState.Ready)?.candidates.orEmpty()
    val videoStreams = candidates.filterNot { it.mimeType.startsWith("audio") }
    val audioStreams = candidates.filter { it.mimeType.startsWith("audio") }
    val analyzed = resolveState is ResolveUiState.Ready && candidates.isNotEmpty()

    fun goTo(route: String) {
        // شاشات القائمة السفلية بترجع لأول الـ back stack بدل ما تتكدس فوق بعض
        if (route == Routes.HOME || route == Routes.DOWNLOADS || route == Routes.SETTINGS) {
            navController.navigate(route) {
                popUpTo(Routes.HOME) { inclusive = false }
                launchSingleTop = true
            }
        } else {
            navController.navigate(route)
        }
    }

    NavHost(navController = navController, startDestination = Routes.HOME) {

        composable(Routes.HOME) {
            HomeScreen(
                linkText = viewModel.linkText,
                onLinkChange = viewModel::onLinkChange,
                onDownloadClick = {
                    if (viewModel.linkText.isNotBlank()) goTo(Routes.LINK_ANALYSIS)
                },
                onNavigate = { key ->
                    when (key) {
                        "Home" -> goTo(Routes.HOME)
                        "Tools" -> goTo(Routes.LIBRARY)
                        "Downloads" -> goTo(Routes.DOWNLOADS)
                        "Settings" -> goTo(Routes.SETTINGS)
                    }
                }
            )
        }

        composable(Routes.LINK_ANALYSIS) {
            LinkAnalysisScreen(
                linkText = viewModel.linkText,
                onLinkChange = viewModel::onLinkChange,
                isAnalyzed = analyzed,
                videoInfo = videoInfo,
                onAnalyzeClick = viewModel::analyze,
                onContinueClick = { goTo(Routes.QUALITY) },
                onBack = { navController.popBackStack() },
                analyzeState = resolveState.toAnalyzeUi()
            )
        }

        composable(Routes.QUALITY) {
            QualitySelectionScreen(
                videoInfo = videoInfo,
                selectedQuality = selectedQuality,
                onQualitySelected = { selectedQuality = it },
                audioOnlySelected = audioOnly,
                onAudioOnlyToggle = { audioOnly = it },
                onStartDownload = {
                    // Real behaviour: queue the best matching stream (or the
                    // audio track when audio-only is picked) and jump to the
                    // live queue. The mock ladder only shows when no real
                    // stream list exists.
                    when {
                        audioOnly && audioStreams.isNotEmpty() -> viewModel.enqueue(audioStreams.first())
                        videoStreams.isNotEmpty() -> {
                            val match = videoStreams.firstOrNull {
                                it.quality?.startsWith(selectedQuality.substringBefore(" ")) == true
                            } ?: videoStreams.first()
                            viewModel.enqueue(match)
                        }
                        else -> Unit
                    }
                    goTo(Routes.DOWNLOADS)
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.ADVANCED) {
            AdvancedOptionsScreen(
                playlistEnabled = playlistEnabled,
                onPlaylistToggle = { playlistEnabled = it },
                downloadMultipleEnabled = downloadMultiple,
                onDownloadMultipleToggle = { downloadMultiple = it },
                convertToMp4 = convertToMp4,
                onConvertToggle = { convertToMp4 = it },
                saveFolderPath = "/Download/Video",
                fileNameMode = "Use original name",
                onApply = {
                    if (downloadMultiple) viewModel.enqueueAll(candidates)
                    goTo(Routes.DOWNLOADS)
                },
                onBack = { navController.popBackStack() },
                onOpenPlaylist = { goTo(Routes.PLAYLIST) }
            )
        }

        composable(Routes.DOWNLOADS) {
            DownloadsScreen(
                onNavigate = { key ->
                    when (key) {
                        "Home" -> goTo(Routes.HOME)
                        "Tools" -> goTo(Routes.LIBRARY)
                        "Downloads" -> goTo(Routes.DOWNLOADS)
                        "Settings" -> goTo(Routes.SETTINGS)
                    }
                }
            )
        }

        composable(Routes.PLAYER) {
            PlayerScreen(
                videoInfo = videoInfo,
                isPlaying = isPlaying,
                onPlayPauseToggle = { isPlaying = !isPlaying },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.PLAYLIST) {
            val selectedCount = playlistVideos.count { it.selected }
            PlaylistScreen(
                videos = playlistVideos,
                onToggleSelect = { index ->
                    val i = playlistVideos.indexOfFirst { it.index == index }
                    if (i != -1) playlistVideos[i] = playlistVideos[i].copy(selected = !playlistVideos[i].selected)
                },
                onSelectAll = {
                    for (i in playlistVideos.indices) playlistVideos[i] = playlistVideos[i].copy(selected = true)
                },
                selectedCount = selectedCount,
                selectedTotalSize = "370 MB",
                onDownloadSelected = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.LIBRARY) {
            LibraryScreen(
                onBack = { navController.popBackStack() },
                onItemClick = { goTo(Routes.PLAYER) }
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                defaultQuality = "1080p (Full HD)",
                defaultFormat = "MP4",
                defaultAudioQuality = "320 kbps",
                downloadFolder = "/Download/Video",
                notificationsEnabled = notificationsEnabled,
                onNotificationsToggle = { notificationsEnabled = it },
                autoDownloadEnabled = autoDownloadEnabled,
                onAutoDownloadToggle = { autoDownloadEnabled = it },
                cacheSize = "12.4 MB",
                onClearCache = { viewModel.clearFinished() },
                onAbout = { goTo(Routes.BRANDING) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.BRANDING) {
            BrandingScreen()
        }
    }
}

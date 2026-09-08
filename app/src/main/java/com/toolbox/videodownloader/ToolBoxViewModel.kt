package com.toolbox.videodownloader

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.toolbox.videodownloader.data.DownloadRepository
import com.toolbox.videodownloader.extractor.StreamExtractor
import com.toolbox.videodownloader.model.DownloadItem
import com.toolbox.videodownloader.model.DownloadStatus
import com.toolbox.videodownloader.resolve.MediaResolver
import com.toolbox.videodownloader.resolve.ResolveResult
import com.toolbox.videodownloader.resolve.ResolvedMedia
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID

/** What the analyze step found behind the pasted link. */
sealed interface ResolveUiState {
    data object Idle : ResolveUiState
    data object Working : ResolveUiState
    data class Ready(val candidates: List<ResolvedMedia>) : ResolveUiState
    data class Error(val message: String) : ResolveUiState
}

/**
 * Bridges the ToolBox screens to the real download machinery: resolve puts a
 * link into streams, the queue screen watches the repository, and the engine
 * service does the actual transfer.
 */
class ToolBoxViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DownloadRepository.get(application)
    private val resolver = MediaResolver()

    val downloads: StateFlow<List<DownloadItem>> = repository.items

    var linkText by mutableStateOf("")
        private set

    var resolveState by mutableStateOf<ResolveUiState>(ResolveUiState.Idle)
        private set

    fun onLinkChange(value: String) {
        linkText = value
        if (resolveState !is ResolveUiState.Idle) resolveState = ResolveUiState.Idle
    }

    fun analyze() {
        val target = linkText.trim()
        if (target.isEmpty()) return
        resolveState = ResolveUiState.Working
        viewModelScope.launch {
            when (val result = resolver.resolve(target)) {
                is ResolveResult.Success -> {
                    resolveState = if (result.candidates.isEmpty()) {
                        ResolveUiState.Error("No downloadable media was found on that page.")
                    } else {
                        ResolveUiState.Ready(result.candidates)
                    }
                }
                is ResolveResult.Failure ->
                    resolveState = ResolveUiState.Error(result.reason)
            }
        }
    }

    fun dismissResolve() {
        resolveState = ResolveUiState.Idle
    }

    fun clearError() {
        if (resolveState is ResolveUiState.Error) resolveState = ResolveUiState.Idle
    }

    /** Queues one resolved stream and starts it immediately. */
    fun enqueue(media: ResolvedMedia) {
        val context = getApplication<Application>()
        val id = UUID.randomUUID().toString()
        val directory = File(
            context.getExternalFilesDir(null) ?: context.filesDir,
            "downloads",
        ).apply { mkdirs() }

        repository.add(
            DownloadItem(
                id = id,
                sourceUrl = linkText.trim(),
                mediaUrl = media.mediaUrl,
                title = media.title,
                fileName = media.fileName,
                mimeType = media.mimeType,
                partPath = File(directory, "$id.part").absolutePath,
                totalBytes = media.sizeBytes,
                thumbnailUrl = media.thumbnailUrl,
            ),
        )
        EngineStarter.start(context, id)
        linkText = ""
        resolveState = ResolveUiState.Idle
    }

    fun enqueueAll(candidates: List<ResolvedMedia>) {
        candidates.forEach { enqueue(it) }
    }

    fun pause(id: String) = repository.update(id) { it.copy(status = DownloadStatus.Paused) }

    fun resume(id: String) {
        repository.update(id) { it.copy(status = DownloadStatus.Queued, errorMessage = null) }
        EngineStarter.start(getApplication(), id)
    }

    fun retry(id: String) {
        repository.update(id) {
            it.copy(
                status = DownloadStatus.Queued,
                errorMessage = null,
                downloadedBytes = 0,
            )
        }
        EngineStarter.start(getApplication(), id)
    }

    fun cancel(id: String) = repository.remove(id)

    fun removeCompleted(id: String) = repository.remove(id)

    fun clearFinished() = repository.clearFinished()
}

package com.chan.echojournal.echos.presentation.models

import com.chan.echojournal.echos.presentation.echos.models.PlaybackState
import com.chan.echojournal.echos.presentation.util.toReadableTime
import java.time.Instant
import kotlin.time.Duration

data class EchoUi(
    val id: Int,
    val title: String,
    val mood: MoodUI,
    val recordedAt: Instant,
    val note: String?,
    val topics: List<String>,
    val amplitudes: List<Float>,
    val audioFilePath: String,
    val playbackTotalDuration: Duration,
    val playbackCurrentDuration: Duration = Duration.ZERO,
    val playbackState: PlaybackState = PlaybackState.STOPPED
) {
    val formattedRecordedAt = recordedAt.toReadableTime()
    val playbackRatio = (playbackCurrentDuration / playbackTotalDuration).toFloat()
}

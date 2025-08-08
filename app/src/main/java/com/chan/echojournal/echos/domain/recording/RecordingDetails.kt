package com.chan.echojournal.echos.domain.recording

import kotlin.time.Duration

data class RecordingDetails(
    val durations: Duration = Duration.ZERO,
    val amplitudes: List<Float> = emptyList(),
    val filePath: String? = null
)

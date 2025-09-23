package com.chan.echojournal.echos.domain.datasource

import java.time.Instant
import kotlin.time.Duration

data class Echo(
    val mood: Mood,
    val id: Int? = null,
    val title: String,
    val note: String?,
    val topics: List<String>,
    val audioFilePath: String,
    val recordedAt: Instant,
    val audioPlaybackLength: Duration,
    val audioAmplitudes: List<Float>,
)

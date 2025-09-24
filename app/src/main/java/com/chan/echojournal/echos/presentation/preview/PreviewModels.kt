package com.chan.echojournal.echos.presentation.preview

import com.chan.echojournal.echos.presentation.echos.models.PlaybackState
import com.chan.echojournal.echos.presentation.models.EchoUi
import com.chan.echojournal.echos.presentation.models.MoodUI
import java.time.Instant
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

data object PreviewModels {
    val echoUi = EchoUi(
        id = 0,
        title = "My audio memo",
        mood = MoodUI.STRESSED,
        recordedAt = Instant.now(),
        note = (1..50).map { "Hello" }.joinToString(" "),
        topics = listOf("Love", "Work"),
        amplitudes = (1..30).map { Random.nextFloat() },
        playbackTotalDuration = 250.seconds,
        playbackCurrentDuration = 125.seconds,
        playbackState = PlaybackState.STOPPED,
        audioFilePath = ""
    )
}
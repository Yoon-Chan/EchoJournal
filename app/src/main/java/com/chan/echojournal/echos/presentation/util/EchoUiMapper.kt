package com.chan.echojournal.echos.presentation.util

import com.chan.echojournal.echos.domain.datasource.Echo
import com.chan.echojournal.echos.presentation.echos.models.PlaybackState
import com.chan.echojournal.echos.presentation.models.EchoUi
import com.chan.echojournal.echos.presentation.models.MoodUI
import kotlin.time.Duration

fun Echo.toEchoUi(
    currentPlaybackDuration: Duration = Duration.ZERO,
    playbackState: PlaybackState = PlaybackState.STOPPED
): EchoUi {
    return EchoUi(
        id = id!!,
        title = title,
        mood = MoodUI.valueOf(mood.name),
        recordedAt = recordedAt,
        note = note,
        topics = topics,
        amplitudes = audioAmplitudes,
        audioFilePath = audioFilePath,
        playbackTotalDuration = audioPlaybackLength,
        playbackCurrentDuration = currentPlaybackDuration,
        playbackState = playbackState
    )
}
package com.chan.echojournal.core.presentation.util

import com.chan.echojournal.app.navigation.NavigationRoute
import com.chan.echojournal.echos.domain.recording.RecordingDetails
import kotlin.time.Duration.Companion.milliseconds

fun RecordingDetails.tooCreateEchoRoute(): NavigationRoute.CreateEcho {
    return NavigationRoute.CreateEcho(
        recordingPath = filePath ?: throw IllegalArgumentException("Recording path can't be null"),
        duration = this.durations.inWholeMilliseconds,
        amplitudes = this.amplitudes.joinToString(";")
    )
}

fun NavigationRoute.CreateEcho.toRecordingDetails(): RecordingDetails {
    return RecordingDetails(
        durations = this.duration.milliseconds,
        amplitudes = this.amplitudes.split(";").map { it.toFloat() },
        filePath = this.recordingPath
    )
}
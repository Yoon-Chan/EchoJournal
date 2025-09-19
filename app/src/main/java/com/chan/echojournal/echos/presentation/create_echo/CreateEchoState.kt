package com.chan.echojournal.echos.presentation.create_echo

import com.chan.echojournal.echos.presentation.echos.models.PlaybackState
import com.chan.echojournal.echos.presentation.models.MoodUI
import kotlin.time.Duration

data class CreateEchoState(
    val titleText: String = "",
    val addTopicText: String = "",
    val noteText: String = "",
    val showMoodSelector: Boolean = true,
    val selectedMood: MoodUI = MoodUI.NEUTRAL,
    val showTopicSuggestion: Boolean = false,
    val mood: MoodUI? = null,
    val searchResults: List<String> = emptyList(),
    val showCreateTopicOption: Boolean = false,
    val canSaveEcho: Boolean = false,
    val playbackAmplitudes: List<Float> = emptyList(),
    val playbackTotalDuration: Duration = Duration.ZERO,
    val playbackState: PlaybackState = PlaybackState.STOPPED,
    val durationPlayed: Duration = Duration.ZERO,
) {
    val durationPlayedRatio = (durationPlayed / playbackTotalDuration).toFloat()
}

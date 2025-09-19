package com.chan.echojournal.echos.presentation.create_echo

import com.chan.echojournal.echos.presentation.echos.models.TrackSizeInfo
import com.chan.echojournal.echos.presentation.models.MoodUI

sealed interface CreateEchoAction {
    data object OnNavigateBackClick : CreateEchoAction
    data class OnTitleTextChange(val text: String) : CreateEchoAction
    data class OnAddTopicTextChange(val text: String) : CreateEchoAction
    data class OnNoteTextChange(val text: String) : CreateEchoAction
    data object ShowMoodSelector : CreateEchoAction
    data object OnDismissMoodSelector : CreateEchoAction
    data class OnMoodClick(val moodUI: MoodUI) : CreateEchoAction
    data object OnConfirmMood : CreateEchoAction
    data class OnTopicClick(val topic: String) : CreateEchoAction
    data object OnDismissTopicSuggestions : CreateEchoAction
    data object OnCancelClick : CreateEchoAction
    data object OnSaveClick : CreateEchoAction
    data object OnCreateNewTopicClick : CreateEchoAction
    data object OnPlayAudioClick : CreateEchoAction
    data object OnPauseAudioClick : CreateEchoAction
    data object OnSelectMoodClick : CreateEchoAction
    data class OnTrackSizeAvailable(val trackSizeInfo: TrackSizeInfo) : CreateEchoAction
    data class OnRemoveTopicClick(val topic: String) : CreateEchoAction
}
package com.chan.echojournal.echos.presentation.echos

import com.chan.echojournal.echos.presentation.echos.models.EchoFilterChip
import com.chan.echojournal.echos.presentation.echos.models.TrackSizeInfo
import com.chan.echojournal.echos.presentation.models.MoodUI

sealed interface EchosAction {
    data object OnMoodChipClick : EchosAction
    data object OnDismissMoodChipDropDown : EchosAction
    data class OnFilterByMoodClick(val moodUI: MoodUI) : EchosAction
    data object OnTopicChipClick : EchosAction
    data object OnDismissTopicChipDropDown : EchosAction
    data class OnFilterByTopicClick(val topic: String) : EchosAction
    data object OnRecordFabClick : EchosAction
    data object OnRequestPermissionQuickRecording : EchosAction
    data object OnRecordButtonLongClick : EchosAction
    data object OnSettingsClick : EchosAction
    data class OnRemoveFilters(val filterType: EchoFilterChip) : EchosAction
    data class OnPlayEchoClick(val echoId: Int): EchosAction
    data object OnPauseRecordingClick: EchosAction
    data object OnPauseAudioClick: EchosAction
    data object OnResumeRecordingClick: EchosAction
    data object OnCompleteRecording: EchosAction
    data class OnTrackSizeAvailable(val trackSizeInfo: TrackSizeInfo): EchosAction
    data object OnAudioPermissionGranted: EchosAction
    data object OnCancelRecording: EchosAction
}
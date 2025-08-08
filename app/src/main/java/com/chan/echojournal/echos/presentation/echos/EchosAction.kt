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
    data object OnFabClick : EchosAction
    data object OnFabLongClick : EchosAction
    data object OnSettingsClick : EchosAction
    data class OnRemoveFilters(val filterType: EchoFilterChip) : EchosAction
    data class OnPlayEchoClick(val echoId: Int): EchosAction
    data object OnPauseClick: EchosAction
    data class OnTrackSizeAvailable(val trackSizeInfo: TrackSizeInfo): EchosAction
}
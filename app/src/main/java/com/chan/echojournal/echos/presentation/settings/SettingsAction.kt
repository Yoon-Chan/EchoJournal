package com.chan.echojournal.echos.presentation.settings

import com.chan.echojournal.echos.presentation.models.MoodUI

sealed interface SettingsAction {
    data class OnSearchTextChange(val text: String): SettingsAction
    data object OnCreateTopicClick: SettingsAction
    data class OnRemoveTopicClick(val topic: String): SettingsAction
    data object OnBackClick: SettingsAction
    data object OnDismissTopicDropDown: SettingsAction
    data object OnAddButtonClick: SettingsAction
    data class OnMoodClick(val mood: MoodUI): SettingsAction
}
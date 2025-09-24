package com.chan.echojournal.echos.presentation.settings

import com.chan.echojournal.echos.presentation.models.MoodUI

data class SettingsState(
    val topics: List<String> = emptyList(),
    val selectedMood: MoodUI? = null,
    val searchText: String = "",
    val suggestedTopics: List<String> = emptyList(),
    val isTopicSuggestionVisible: Boolean = false,
    val showCreateTopicOption: Boolean = false,
    val isTopicTextInputVisible: Boolean = false
)
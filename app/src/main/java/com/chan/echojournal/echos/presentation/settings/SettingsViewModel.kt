package com.chan.echojournal.echos.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chan.echojournal.echos.domain.datasource.Mood
import com.chan.echojournal.echos.domain.settings.SettingPreferences
import com.chan.echojournal.echos.presentation.models.MoodUI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsPreferences: SettingPreferences
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(SettingsState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeSettings()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = SettingsState()
        )

    fun onAction(action: SettingsAction) {
        when (action) {
            SettingsAction.OnAddButtonClick -> {}
            is SettingsAction.OnSelectTopic -> onSelectTopic(action.topic)
            SettingsAction.OnDismissTopicDropDown -> {}
            is SettingsAction.OnMoodClick -> onMoodClick(action.mood)
            is SettingsAction.OnRemoveTopicClick -> onRemoveTopicClick(action.topic)
            is SettingsAction.OnSearchTextChange -> {}
            else -> Unit
        }
    }

    private fun onMoodClick(mood: MoodUI) {
        viewModelScope.launch {
            settingsPreferences.saveDefaultMood(Mood.valueOf(mood.name))
        }
    }

    private fun onSelectTopic(topic: String) {
        viewModelScope.launch {
            val newDefaultTopics = (state.value.topics + topic).distinct()
            settingsPreferences.saveDefaultTopics(newDefaultTopics)
        }
    }

    private fun onRemoveTopicClick(topic: String) {
        viewModelScope.launch {
            val newDefaultTopics = (state.value.topics - topic).distinct()
            settingsPreferences.saveDefaultTopics(newDefaultTopics)
        }
    }

    private fun observeSettings() {
        combine(
            settingsPreferences.observeDefaultTopics(),
            settingsPreferences.observeDefaultMood(),
        ) { topics, mood ->
            _state.update {
                it.copy(
                    topics = topics,
                    selectedMood = MoodUI.valueOf(mood.name)
                )
            }
        }.launchIn(viewModelScope)
    }
}
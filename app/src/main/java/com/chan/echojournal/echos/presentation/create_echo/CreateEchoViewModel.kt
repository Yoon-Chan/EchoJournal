package com.chan.echojournal.echos.presentation.create_echo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class CreateEchoViewModel constructor(

): ViewModel() {

    private var hasLoadedInitialData = false
    private val _state = MutableStateFlow(CreateEchoState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = CreateEchoState()
        )

    fun onAction(action: CreateEchoAction) {
        when (action) {
            is CreateEchoAction.OnAddTopicTextChange -> {}
            CreateEchoAction.OnCancelClick -> {}
            CreateEchoAction.OnConfirmMood -> {}
            CreateEchoAction.OnCreateNewTopicClick -> {}
            CreateEchoAction.OnDismissMoodSelector -> {}
            CreateEchoAction.OnDismissTopicSuggestions -> {}
            is CreateEchoAction.OnMoodClick -> {}
            CreateEchoAction.OnNavigateBackClick -> {}
            is CreateEchoAction.OnNoteTextChange -> {}
            CreateEchoAction.OnPauseAudioClick -> {}
            CreateEchoAction.OnPlayAudioClick -> {}
            is CreateEchoAction.OnRemoveTopicClick -> {}
            CreateEchoAction.OnSaveClick -> {}
            is CreateEchoAction.OnTitleTextChange -> {}
            is CreateEchoAction.OnTopicClick -> {}
            is CreateEchoAction.OnTrackSizeAvailable -> {}

            CreateEchoAction.ShowMoodSelector -> {}
            CreateEchoAction.OnSelectMoodClick ->{}
        }
    }
}
package com.chan.echojournal.echos.presentation.create_echo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.chan.echojournal.app.navigation.NavigationRoute
import com.chan.echojournal.core.presentation.designystem.dropdowns.Selectable.Companion.asUnselectedItems
import com.chan.echojournal.core.presentation.util.toRecordingDetails
import com.chan.echojournal.echos.domain.recording.RecordingStorage
import com.chan.echojournal.echos.presentation.models.MoodUI
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateEchoViewModel constructor(
    private val savedStateHandle: SavedStateHandle,
    private val recordingStorage: RecordingStorage
) : ViewModel() {

    private val route = savedStateHandle.toRoute<NavigationRoute.CreateEcho>()
    private val recordingDetails = route.toRecordingDetails()
    private var hasLoadedInitialData = false

    private val _event = Channel<CreateEchoEvent>()
    val event = _event.receiveAsFlow()

    private val _state = MutableStateFlow(CreateEchoState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeAddTopicText()
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
            is CreateEchoAction.OnAddTopicTextChange -> onAddTopicTextChange(action.text)
            CreateEchoAction.OnConfirmMood -> onConfirmMood()
            CreateEchoAction.OnDismissMoodSelector -> onDismissMoodSelector()

            CreateEchoAction.OnDismissTopicSuggestions -> onDismissTopicSuggestion()
            is CreateEchoAction.OnMoodClick -> onClickMoodClick(action.moodUI)
            is CreateEchoAction.OnNoteTextChange -> {}
            CreateEchoAction.OnPauseAudioClick -> {}
            CreateEchoAction.OnPlayAudioClick -> {}
            is CreateEchoAction.OnRemoveTopicClick -> onRemoveTopicClick(action.topic)
            CreateEchoAction.OnSaveClick -> onSaveClick()
            is CreateEchoAction.OnTitleTextChange -> onTitleTextChange(action.text)
            is CreateEchoAction.OnTopicClick -> onTopicClick(action.topic)
            is CreateEchoAction.OnTrackSizeAvailable -> {}

            CreateEchoAction.ShowMoodSelector -> {}
            CreateEchoAction.OnSelectMoodClick -> onSelectMoodClick()
            CreateEchoAction.OnDismissConfirmLeaveDialog -> onDismissConfirmLeaveDialog()

            CreateEchoAction.OnCancelClick,
            CreateEchoAction.OnNavigateBackClick,
            CreateEchoAction.OnGoBack -> onShowConfirmLeaveDialog()
        }
    }

    private fun onTitleTextChange(text: String) {
        _state.update {
            it.copy(
                titleText = text
            )
        }
    }

    private fun onSaveClick() {
        if(recordingDetails.filePath == null) {
            return
        }

        viewModelScope.launch {
            val savedFilePath = recordingStorage.savePersistently(
                tempFilePath = recordingDetails.filePath
            )
            if(savedFilePath == null) {
                _event.send(CreateEchoEvent.FailedToSaveFile)
                return@launch
            }

            //TODO: Echo
        }
    }

    private fun onDismissConfirmLeaveDialog() {
        _state.update {
            it.copy(
                showConfirmLeaveDialog = false
            )
        }
    }

    private fun onShowConfirmLeaveDialog() {
        _state.update {
            it.copy(
                showConfirmLeaveDialog = true
            )
        }
    }

    private fun observeAddTopicText() {
        _state
            .map { it.addTopicText }
            .distinctUntilChanged()
            .debounce(300L)
            .onEach { query ->
                _state.update {
                    it.copy(
                        showTopicSuggestion = query.isNotBlank() && query.trim() !in it.topics,
                        searchResults = listOf(
                            "hello",
                            "heeloworld"
                        ).asUnselectedItems()
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun onDismissTopicSuggestion() {
        _state.update {
            it.copy(
                showTopicSuggestion = false
            )
        }
    }

    private fun onRemoveTopicClick(topic: String) {
        _state.update {
            it.copy(
                topics = it.topics - topic
            )
        }
    }

    private fun onTopicClick(topic: String) {
        _state.update {
            it.copy(
                addTopicText = "",
                topics = (it.topics + topic).distinct()
            )
        }
    }

    private fun onAddTopicTextChange(text: String) {
        _state.update {
            it.copy(
                addTopicText = text.filter {
                    it.isLetterOrDigit()
                }
            )
        }
    }

    private fun onConfirmMood() {
        _state.update {
            it.copy(
                mood = it.selectedMood,
                canSaveEcho = it.titleText.isNotBlank(),
                showMoodSelector = false
            )
        }
    }

    private fun onDismissMoodSelector() {
        _state.update {
            it.copy(
                showMoodSelector = false
            )
        }
    }

    private fun onSelectMoodClick() {
        _state.update {
            it.copy(showMoodSelector = true)
        }
    }

    private fun onClickMoodClick(moodUI: MoodUI) {
        _state.update {
            it.copy(selectedMood = moodUI)
        }
    }
}
package com.chan.echojournal.echos.presentation.echos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chan.echojournal.R
import com.chan.echojournal.core.presentation.designystem.dropdowns.Selectable
import com.chan.echojournal.core.presentation.util.UiText
import com.chan.echojournal.echos.domain.audio.AudioPlayer
import com.chan.echojournal.echos.domain.datasource.Echo
import com.chan.echojournal.echos.domain.datasource.EchoDataSource
import com.chan.echojournal.echos.domain.recording.VoiceRecorder
import com.chan.echojournal.echos.presentation.echos.models.AudioCaptureMethod
import com.chan.echojournal.echos.presentation.echos.models.EchoFilterChip
import com.chan.echojournal.echos.presentation.echos.models.MoodChipContent
import com.chan.echojournal.echos.presentation.echos.models.PlaybackState
import com.chan.echojournal.echos.presentation.echos.models.RecordingState
import com.chan.echojournal.echos.presentation.echos.models.TrackSizeInfo
import com.chan.echojournal.echos.presentation.models.EchoUi
import com.chan.echojournal.echos.presentation.models.MoodUI
import com.chan.echojournal.echos.presentation.util.AmplitudeNormalizer
import com.chan.echojournal.echos.presentation.util.toEchoUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class EchosViewModel constructor(
    private val voiceRecorder: VoiceRecorder,
    private val audioPlayer: AudioPlayer,
    private val echoDataSource: EchoDataSource
) : ViewModel() {

    companion object {
        private val MIN_RECORD_DURATION = 1.5.seconds
    }

    private var hasLoadedInitialData = false

    private val playingEchoId = MutableStateFlow<Int?>(null)
    private val selectedMoodFilters = MutableStateFlow<List<MoodUI>>(emptyList())
    private val selectedTopicFilters = MutableStateFlow<List<String>>(emptyList())
    private val audioTrackSizeInfo = MutableStateFlow<TrackSizeInfo?>(null)
    private val eventChannel = Channel<EchosEvent>()
    val events = eventChannel.receiveAsFlow()

    private val _state = MutableStateFlow(EchosState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeFilters()
                observeEchos()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = EchosState()
        )

    private val filteredEchos = echoDataSource
        .observeEchos()
        .filterByMoodAndTopics()
        .onEach { echos ->
            _state.update {
                it.copy(
                    hasEchosRecord = echos.isNotEmpty(),
                    isLoadingData = false
                )
            }
        }
        .combine(audioTrackSizeInfo) { echos, trackSizeInfo ->
            trackSizeInfo?.let {
                echos.map { echo ->
                    echo.copy(
                        audioAmplitudes = AmplitudeNormalizer.normalize(
                            sourceAmplitudes = echo.audioAmplitudes,
                            trackWidth = it.trackWidth,
                            barWidth = it.barWidth,
                            spacing = it.spacing
                        )
                    )
                }
            } ?: echos
        }
        .flowOn(Dispatchers.Default)

    fun onAction(action: EchosAction) {
        when (action) {
            EchosAction.OnRecordFabClick -> {
                requestAudioPermission()
                _state.update {
                    it.copy(
                        currentCaptureMethod = AudioCaptureMethod.STANDARD
                    )
                }
            }

            EchosAction.OnRequestPermissionQuickRecording -> {
                requestAudioPermission()
                _state.update {
                    it.copy(
                        currentCaptureMethod = AudioCaptureMethod.QUICK
                    )
                }
            }

            EchosAction.OnRecordButtonLongClick -> {
                startRecording(captureMethod = AudioCaptureMethod.QUICK)
            }

            is EchosAction.OnRemoveFilters -> {
                when (action.filterType) {
                    EchoFilterChip.MOODS -> selectedMoodFilters.update { emptyList() }
                    EchoFilterChip.TOPICS -> selectedTopicFilters.update { emptyList() }
                }
            }

            EchosAction.OnMoodChipClick -> {
                _state.update {
                    it.copy(
                        selectedEchoFilterChip = EchoFilterChip.MOODS
                    )
                }
            }

            EchosAction.OnTopicChipClick -> {
                _state.update {
                    it.copy(
                        selectedEchoFilterChip = EchoFilterChip.TOPICS
                    )
                }
            }

            EchosAction.OnSettingsClick -> {}
            EchosAction.OnDismissTopicChipDropDown,
            EchosAction.OnDismissMoodChipDropDown -> {
                _state.update {
                    it.copy(
                        selectedEchoFilterChip = null,
                    )
                }
            }

            is EchosAction.OnFilterByMoodClick -> {
                toggleMoodFilter(action.moodUI)
                _state.update {
                    it.copy(
                        selectedEchoFilterChip = EchoFilterChip.MOODS
                    )
                }
            }

            is EchosAction.OnFilterByTopicClick -> {
                toggleTopicFilter(action.topic)
                _state.update {
                    it.copy(
                        selectedEchoFilterChip = EchoFilterChip.TOPICS
                    )
                }
            }

            is EchosAction.OnPlayEchoClick -> onPlayEchoClick(action.echoId)
            EchosAction.OnPauseAudioClick -> audioPlayer.pause()
            is EchosAction.OnTrackSizeAvailable -> {
                audioTrackSizeInfo.update { action.trackSizeInfo }
            }

            EchosAction.OnAudioPermissionGranted -> {
                startRecording(captureMethod = AudioCaptureMethod.STANDARD)
            }

            EchosAction.OnPauseRecordingClick -> pauseRecording()
            EchosAction.OnCancelRecording -> cancelRecording()
            EchosAction.OnCompleteRecording -> stopRecording()
            EchosAction.OnResumeRecordingClick -> resumeRecording()
        }
    }

    private fun observeEchos() {
        combine(
            filteredEchos,
            playingEchoId,
            audioPlayer.activeTrack
        ) { echos, playingEchoId, activeTrack ->
            if (playingEchoId == null || activeTrack == null) {
                return@combine echos.map { it.toEchoUi() }
            }

            echos.map { echo ->
                if (echo.id == playingEchoId) {
                    echo.toEchoUi(
                        currentPlaybackDuration = activeTrack.durationPlayed,
                        playbackState = if (activeTrack.isPlaying) PlaybackState.PLAYING else PlaybackState.PAUSED
                    )
                } else echo.toEchoUi()
            }
        }.groupByRelativeDate()
            .onEach { groupedEchos ->
                _state.update {
                    it.copy(
                        echos = groupedEchos
                    )
                }
            }
            .flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)
    }

    private fun onPlayEchoClick(echoId: Int) {
        val selectedEcho = state.value.echos.values.flatten().first { it.id == echoId }
        val activeTrack = audioPlayer.activeTrack.value
        val isNewEcho = playingEchoId.value != echoId
        val isSameEchoIsPlayingFromBeginning = echoId == playingEchoId.value && activeTrack != null
                && activeTrack.durationPlayed == Duration.ZERO

        when {
            isNewEcho || isSameEchoIsPlayingFromBeginning -> {
                playingEchoId.update { echoId }
                audioPlayer.stop()
                audioPlayer.play(
                    filePath = selectedEcho.audioFilePath,
                    onComplete = ::completePlayback
                )
            }

            else -> audioPlayer.resume()
        }

    }

    private fun completePlayback() {
        _state.update {
            it.copy(
                echos = it.echos.mapValues { (_, echos) ->
                    echos.map { echo ->
                        echo.copy(
                            playbackCurrentDuration = Duration.ZERO
                        )
                    }
                }
            )
        }
        playingEchoId.update { null }
    }

    private fun requestAudioPermission() = viewModelScope.launch {
        eventChannel.send(EchosEvent.RequestAudioPermission)
    }

    private fun pauseRecording() {
        voiceRecorder.pause()
        _state.update {
            it.copy(
                recordingState = RecordingState.PAUSED
            )
        }
    }

    private fun resumeRecording() {
        voiceRecorder.resume()
        _state.update {
            it.copy(
                recordingState = RecordingState.NORMAL_CAPTURE
            )
        }
    }

    private fun cancelRecording() {
        _state.update {
            it.copy(
                recordingState = RecordingState.NOT_RECORDING,
                currentCaptureMethod = null
            )
        }
        voiceRecorder.cancel()
    }

    private fun stopRecording() {
        voiceRecorder.stop()
        _state.update { it.copy(recordingState = RecordingState.NOT_RECORDING) }

        val recordingDetails = voiceRecorder.recordingDetails.value
        viewModelScope.launch {
            if (recordingDetails.durations < MIN_RECORD_DURATION) {
                eventChannel.send(EchosEvent.RecordingTooShort)
            } else {
                eventChannel.send(EchosEvent.OnDoneRecording(recordingDetails))
            }
        }
    }

    private fun startRecording(captureMethod: AudioCaptureMethod) {
        _state.update {
            it.copy(
                recordingState = when (captureMethod) {
                    AudioCaptureMethod.STANDARD -> RecordingState.NORMAL_CAPTURE
                    AudioCaptureMethod.QUICK -> RecordingState.QUICK_CAPTURE
                }
            )
        }
        voiceRecorder.start()

        if (captureMethod == AudioCaptureMethod.STANDARD) {
            voiceRecorder
                .recordingDetails
                .distinctUntilChangedBy { it.durations }
                .map { it.durations }
                .onEach { duration ->
                    _state.update { it.copy(recordingElapsedDuration = duration) }
                }
                .launchIn(viewModelScope)
        }
    }

    private fun toggleMoodFilter(moodUI: MoodUI) {
        selectedMoodFilters.update { selectedMoods ->
            if (moodUI in selectedMoods) {
                selectedMoods - moodUI
            } else {
                selectedMoods + moodUI
            }
        }
    }

    private fun toggleTopicFilter(topic: String) {
        selectedTopicFilters.update { selectedTopic ->
            if (topic in selectedTopic) {
                selectedTopic - topic
            } else {
                selectedTopic + topic
            }
        }
    }

    private fun observeFilters() {
        combine(
            echoDataSource.observeTopics(),
            selectedTopicFilters,
            selectedMoodFilters
        ) { allTopics, selectedTopics, selectedMoods ->
            _state.update {
                it.copy(
                    topics = allTopics.map { topic ->
                        Selectable(
                            item = topic,
                            selected = selectedTopics.contains(topic)
                        )
                    },
                    moods = MoodUI.entries.map { mood ->
                        Selectable(
                            item = mood,
                            selected = selectedMoods.contains(mood)
                        )
                    },
                    hasActiveMoodFilters = selectedMoods.isNotEmpty(),
                    hasActiveTopicFilters = selectedTopics.isNotEmpty(),
                    topicChipTitle = selectedTopics.deriveTopicsToText(),
                    moodChipContent = selectedMoods.asMoodChipContent()
                )
            }

        }.launchIn(viewModelScope)
    }

    private fun List<String>.deriveTopicsToText(): UiText {
        return when (size) {
            0 -> UiText.StringResource(R.string.all_topics)
            1 -> UiText.Dynamic(this.first())
            2 -> UiText.Dynamic("${this.first()}, ${this.last()}")
            else -> {
                val extraElement = size - 2
                UiText.Dynamic("${this.first()}, ${this[1]} +$extraElement")
            }
        }
    }

    private fun List<MoodUI>.asMoodChipContent(): MoodChipContent {
        if (this.isEmpty()) {
            return MoodChipContent()
        }

        val icons = this.map { it.iconSet.fill }
        val moodNames = this.map { it.title }

        return when (size) {
            1 -> MoodChipContent(
                iconsRes = icons,
                title = moodNames.first()
            )

            2 -> MoodChipContent(
                iconsRes = icons,
                title = UiText.Combined(
                    format = "%s, %s",
                    uiTexts = moodNames.toList()
                )
            )

            else -> {
                val extraElementCount = size - 2
                MoodChipContent(
                    iconsRes = icons,
                    title = UiText.Combined(
                        format = "%s, %s +$extraElementCount",
                        uiTexts = moodNames.take(2).toList()
                    )
                )
            }
        }
    }

    private fun Flow<List<Echo>>.filterByMoodAndTopics(): Flow<List<Echo>> {
        return combine(
            this,
            selectedTopicFilters,
            selectedMoodFilters
        ) { echos, topicFilters, moodFilters ->
            echos.filter { echo ->
                val matchesMoodFilter = moodFilters
                    .takeIf { it.isNotEmpty() }
                    ?.any { it.name == echo.mood.name }
                    ?: true

                val matchesTopicFilter = topicFilters
                    .takeIf { it.isNotEmpty() }
                    ?.any { it in echo.topics }
                    ?: true

                matchesMoodFilter && matchesTopicFilter
            }
        }
    }

    private fun Flow<List<EchoUi>>.groupByRelativeDate(): Flow<Map<UiText, List<EchoUi>>> {
        val formatter = DateTimeFormatter.ofPattern("dd MMM")
        val today = LocalDate.now()
        return map { echos ->
            echos.groupBy { echo ->
                //이건 API 34이상에서만 사용 가능함
//                LocalDate.ofInstant(
//                    echo.recordedAt,
//                    ZoneId.systemDefault()
//                )
                echo.recordedAt
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
            }
                .mapValues { (_, echos) ->
                    echos.sortedByDescending { it.recordedAt }

                }
                .toSortedMap(compareByDescending { it })
                .mapKeys { (date, _) ->
                    when (date) {
                        today -> UiText.StringResource(R.string.today)
                        today.minusDays(1) -> UiText.StringResource(R.string.yesterday)
                        else -> UiText.Dynamic(date.format(formatter))
                    }
                }
        }
    }
}
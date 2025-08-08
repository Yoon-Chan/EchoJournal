package com.chan.echojournal.echos.presentation.echos

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chan.echojournal.core.presentation.designystem.theme.EchoJournalTheme
import com.chan.echojournal.core.presentation.designystem.theme.bgGradient
import com.chan.echojournal.core.presentation.util.ObserveAsEvents
import com.chan.echojournal.echos.EchosEvent
import com.chan.echojournal.echos.presentation.echos.components.EchoFilterRow
import com.chan.echojournal.echos.presentation.echos.components.EchoList
import com.chan.echojournal.echos.presentation.echos.components.EchoRecordFloatingActionButton
import com.chan.echojournal.echos.presentation.echos.components.EchosEmptyBackground
import com.chan.echojournal.echos.presentation.echos.components.EchosTopBar
import com.chan.echojournal.echos.presentation.echos.models.AudioCaptureMethod
import org.koin.androidx.compose.koinViewModel

@Composable
fun EchosRoot(
    viewModel: EchosViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted && state.currentCaptureMethod == AudioCaptureMethod.STANDARD) {
            viewModel.onAction(EchosAction.OnAudioPermissionGranted)
        }
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is EchosEvent.RequestAudioPermission -> {
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    EchosScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun EchosScreen(
    state: EchosState,
    onAction: (EchosAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            EchoRecordFloatingActionButton(
                onClick = {
                    onAction(EchosAction.OnFabClick)
                }
            )
        },
        topBar = {
            EchosTopBar(
                onSettingsClick = { onAction(EchosAction.OnSettingsClick) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = MaterialTheme.colorScheme.bgGradient
                )
                .padding(innerPadding)
        ) {
            EchoFilterRow(
                moodChipContent = state.moodChipContent,
                hasActiveMoodFilters = state.hasActiveMoodFilters,
                selectedEchoFilterChip = state.selectedEchoFilterChip,
                moods = state.moods,
                topicChipTitle = state.topicChipTitle,
                hasActiveTopicFilters = state.hasActiveTopicFilters,
                topics = state.topics,
                onAction = onAction,
                modifier = Modifier
                    .fillMaxWidth()
            )

            when {
                state.isLoadingData -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .wrapContentSize(),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                !state.hasEchosRecord -> {
                    EchosEmptyBackground(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                }

                else -> {
                    EchoList(
                        sections = state.echoDaySections,
                        onPlayClick = {
                            onAction(EchosAction.OnPlayEchoClick(it))
                        },
                        onPauseClick = {
                            onAction(EchosAction.OnPauseClick)
                        },
                        onTrackSizeAvailable = { trackSizeInfo ->
                            onAction(EchosAction.OnTrackSizeAvailable(trackSizeInfo))
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    EchoJournalTheme {
        EchosScreen(
            state = EchosState(
                isLoadingData = false,
                hasEchosRecord = false,
            ),
            onAction = {}
        )
    }
}
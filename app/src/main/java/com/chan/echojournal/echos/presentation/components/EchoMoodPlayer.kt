package com.chan.echojournal.echos.presentation.components

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.chan.echojournal.core.presentation.designystem.theme.EchoJournalTheme
import com.chan.echojournal.core.presentation.designystem.theme.MoodPrimary25
import com.chan.echojournal.core.presentation.designystem.theme.MoodPrimary35
import com.chan.echojournal.core.presentation.designystem.theme.MoodPrimary80
import com.chan.echojournal.core.presentation.util.formatMMSS
import com.chan.echojournal.echos.presentation.echos.models.PlaybackState
import com.chan.echojournal.echos.presentation.echos.models.TrackSizeInfo
import com.chan.echojournal.echos.presentation.models.MoodUI
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
fun EchoMoodPlayer(
    moodUI: MoodUI?,
    playbackState: PlaybackState,
    playerProgress: () -> Float,
    durationPlayed: Duration,
    totalPlaybackDuration: Duration,
    powerRatios: List<Float>,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    onTrackSizeAvailable: (TrackSizeInfo) -> Unit,
    modifier: Modifier = Modifier,
    amplitudeBarWidth: Dp = 5.dp,
    amplitudeBarSpacing: Dp = 4.dp,
) {
    val iconTint = when (moodUI) {
        null -> MoodPrimary35
        else -> moodUI.colorSet.vivid
    }

    val trackFillColor = when (moodUI) {
        null -> MoodPrimary80
        else -> moodUI.colorSet.vivid
    }

    val backgroundColor = when (moodUI) {
        null -> MoodPrimary25
        else -> moodUI.colorSet.faded
    }

    val trackColor = when (moodUI) {
        null -> MoodPrimary35
        else -> moodUI.colorSet.desaturated
    }
    val formattedDurationText = remember(durationPlayed, totalPlaybackDuration) {
        "${durationPlayed.formatMMSS()}/${totalPlaybackDuration.formatMMSS()}"
    }
    val density = LocalDensity.current

    Surface(
        shape = CircleShape,
        color = backgroundColor,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Max),
            verticalAlignment = Alignment.CenterVertically
        ) {
            EchoPlaybackButton(
                playbackState = playbackState,
                onPlayClick = onPlayClick,
                onPauseClick = onPauseClick,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = iconTint
                ),
            )

            EchoPlayBar(
                amplitudeBarWidth = amplitudeBarWidth,
                amplitudeBarSpacing = amplitudeBarSpacing,
                trackColor = trackColor,
                trackFillColor = trackFillColor,
                powerRatios = powerRatios,
                playerProgress = playerProgress,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 10.dp, horizontal = 8.dp)
                    .fillMaxHeight()
                    .onSizeChanged {
                        if(it.width > 0) {
                            onTrackSizeAvailable(
                                TrackSizeInfo(
                                    trackWidth = it.width.toFloat(),
                                    barWidth = with(density) {
                                        amplitudeBarWidth.toPx()
                                    },
                                    spacing = with(density) {
                                        amplitudeBarSpacing.toPx()
                                    }
                                )
                            )
                        }
                    }
            )

            Text(
                text = formattedDurationText,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontFeatureSettings = "tnum"
                ),
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

@Preview
@Composable
private fun EchoMoodPlayerPreview() {
    val ratios = remember {
        (1..25).map {
            Random.nextFloat()
        }
    }
    EchoJournalTheme {
        EchoMoodPlayer(
            modifier = Modifier.fillMaxWidth(),
            playbackState = PlaybackState.PLAYING,
            moodUI = MoodUI.SAD,
            playerProgress = { 0.23f },
            durationPlayed = 125.seconds,
            totalPlaybackDuration = 250.seconds,
            powerRatios = ratios,
            onPlayClick = {},
            onPauseClick = {},
            onTrackSizeAvailable = {}
        )
    }
}
package com.chan.echojournal.echos.presentation.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.chan.echojournal.echos.presentation.models.MoodUI

@Composable
fun MoodSelectorRow(
    selectedMood: MoodUI?,
    onMoodClick: (MoodUI) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MoodUI.entries.forEach { mood ->
            MoodItem(
                selected = mood == selectedMood,
                mood = mood,
                onClick = { onMoodClick(mood) }
            )
        }
    }
}
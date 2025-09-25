package com.chan.echojournal.echos.presentation.create_echo.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.chan.echojournal.R
import com.chan.echojournal.core.presentation.designystem.buttons.PrimaryButton
import com.chan.echojournal.core.presentation.designystem.buttons.SecondaryButton
import com.chan.echojournal.echos.presentation.components.MoodSelectorRow
import com.chan.echojournal.echos.presentation.models.MoodUI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectMoodSheet(
    selectedMood: MoodUI,
    onMoodClick: (MoodUI) -> Unit,
    onDismiss: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val allMoods = MoodUI.entries.toList()
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.how_are_you_doing),
                style = MaterialTheme.typography.titleMedium
            )

            MoodSelectorRow(
                selectedMood = selectedMood,
                onMoodClick = onMoodClick
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SecondaryButton(
                    text = stringResource(R.string.cancel),
                    onClick = onDismiss
                )

                PrimaryButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.confirm),
                    onClick = onConfirmClick,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = stringResource(R.string.confirm)
                        )
                    }
                )
            }
        }
    }
}
package com.chan.echojournal.echos.presentation.create_echo.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.chan.echojournal.R
import com.chan.echojournal.core.presentation.designystem.chips.HashtagChip
import com.chan.echojournal.core.presentation.designystem.dropdowns.ExtraSelectableOptionExtras
import com.chan.echojournal.core.presentation.designystem.dropdowns.Selectable
import com.chan.echojournal.core.presentation.designystem.dropdowns.Selectable.Companion.asUnselectedItems
import com.chan.echojournal.core.presentation.designystem.dropdowns.SelectableDropDownOptionsMenu
import com.chan.echojournal.core.presentation.designystem.text_field.TransparentHintTextField
import com.chan.echojournal.core.presentation.designystem.theme.EchoJournalTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EchoTopicRow(
    topics: List<String>,
    addTopicText: String,
    showCreateTopicOption: Boolean,
    showTopicSuggestions: Boolean,
    searchResults: List<Selectable<String>>,
    onTopicClick: (String) -> Unit,
    onDismissTopicSuggestions: () -> Unit,
    onRemoveTopicClick: (String) -> Unit,
    onAddTopicTextChange: (String) -> Unit,
    dropDownMaxHeight: Dp = (LocalConfiguration.current.screenHeightDp * 0.3).dp,
    modifier: Modifier = Modifier
) {
    var topicRowHeight by remember {
        mutableIntStateOf(0)
    }
    var chipHeight by remember {
        mutableIntStateOf(0)
    }
    Row(
        modifier = modifier
            .onSizeChanged {
                topicRowHeight = it.height
            }
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.hashtag),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outlineVariant,
            modifier = Modifier
                .defaultMinSize(
                    minHeight = with(LocalDensity.current) {
                        chipHeight.toDp()
                    }
                )

        )

        FlowRow(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            topics.forEach { topic ->
                HashtagChip(
                    text = topic,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(R.string.remove_topic),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .size(14.dp)
                                .clickable {
                                    onRemoveTopicClick(topic)
                                }
                        )
                    },
                    modifier = Modifier
                        .onSizeChanged {
                            chipHeight = it.height
                        }
                )
            }
            TransparentHintTextField(
                text = addTopicText,
                onValueChange = onAddTopicTextChange,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .defaultMinSize(minHeight = with(LocalDensity.current) { chipHeight.toDp() })
                    .padding(top = 1.5.dp),
                maxLines = 1,
                hintText = if (topics.isEmpty()) {
                    stringResource(R.string.topic)
                } else null
            )
        }

        if (showTopicSuggestions) {
            SelectableDropDownOptionsMenu(
                items = searchResults,
                itemDisplayText = { it },
                onDismiss = onDismissTopicSuggestions,
                key = { it },
                onItemClick = { onTopicClick(it.item) },
                leadingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.hashtag),
                        contentDescription = null,
                    )
                },
                maxDropDownHeight = dropDownMaxHeight,
                dropDownOffset = IntOffset(
                    x = 0,
                    y = topicRowHeight
                ),
                dropDownExtras = if (showCreateTopicOption) {
                    ExtraSelectableOptionExtras(
                        text = addTopicText,
                        onClick = { onTopicClick(addTopicText) }
                    )
                } else null
            )
        }
    }
}

@Preview
@Composable
private fun EchoTopicsRowPreview() {
    EchoJournalTheme {
        EchoTopicRow(
            modifier = Modifier.fillMaxSize(),
            topics = listOf("Work", "Life"),
            addTopicText = "",
            showTopicSuggestions = true,
            showCreateTopicOption = true,
            searchResults = listOf(
                "hello",
                "heeloworld"
            ).asUnselectedItems(),
            onDismissTopicSuggestions = {},
            onRemoveTopicClick = {},
            onAddTopicTextChange = {},
            onTopicClick = {}
        )
    }
}
package com.chan.echojournal.echos.presentation.echos

import com.chan.echojournal.R
import com.chan.echojournal.core.presentation.designystem.dropdowns.Selectable
import com.chan.echojournal.core.presentation.designystem.dropdowns.Selectable.Companion.asUnselectedItems
import com.chan.echojournal.core.presentation.util.UiText
import com.chan.echojournal.echos.presentation.echos.models.EchoDaySection
import com.chan.echojournal.echos.presentation.echos.models.EchoFilterChip
import com.chan.echojournal.echos.presentation.echos.models.MoodChipContent
import com.chan.echojournal.echos.presentation.models.EchoUi
import com.chan.echojournal.echos.presentation.models.MoodUI

data class EchosState(
    val echos: Map<UiText, List<EchoUi>> = emptyMap(),
    val hasEchosRecord: Boolean = false,
    val hasActiveTopicFilters: Boolean = false,
    val hasActiveMoodFilters: Boolean = false,
    val isLoadingData: Boolean = false,
    val moods: List<Selectable<MoodUI>> = emptyList(),
    val topics: List<Selectable<String>> = listOf("Love", "Happy", "Work").asUnselectedItems(),
    val moodChipContent: MoodChipContent = MoodChipContent(),
    val selectedEchoFilterChip: EchoFilterChip? = null,
    val topicChipTitle: UiText = UiText.StringResource(R.string.all_topics)
) {
    val echoDaySections = echos
        .map { (dateHeader, echos) ->
            EchoDaySection(dateHeader, echos)
        }
}
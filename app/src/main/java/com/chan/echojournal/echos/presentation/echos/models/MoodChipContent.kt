package com.chan.echojournal.echos.presentation.echos.models

import com.chan.echojournal.R
import com.chan.echojournal.core.presentation.util.UiText

data class MoodChipContent(
    val iconsRes: List<Int> = emptyList(),
    val title: UiText = UiText.StringResource(R.string.all_moods)
)
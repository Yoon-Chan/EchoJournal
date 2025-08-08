package com.chan.echojournal.echos.presentation.echos.models

import com.chan.echojournal.core.presentation.util.UiText
import com.chan.echojournal.echos.presentation.models.EchoUi

data class EchoDaySection(
    val dateHeader: UiText,
    val echos: List<EchoUi>
)

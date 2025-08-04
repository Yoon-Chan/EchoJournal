package com.chan.echojournal.echos.presentation.echos

data class EchosState(
    val hasEchosRecord: Boolean = false,
    val hasActiveTopicFilters: Boolean = false,
    val hasActiveMoodFilters: Boolean = false,
    val isLoadingData: Boolean = false,
)
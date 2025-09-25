package com.chan.echojournal.echos.domain.settings

import com.chan.echojournal.echos.domain.datasource.Mood
import kotlinx.coroutines.flow.Flow

interface SettingPreferences {
    suspend fun saveDefaultTopics(topics: List<String>)
    fun observeDefaultTopics(): Flow<List<String>>

    suspend fun saveDefaultMood(mood: Mood)
    fun observeDefaultMood(): Flow<Mood>
}
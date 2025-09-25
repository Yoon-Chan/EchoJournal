package com.chan.echojournal.echos.data.settings

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.chan.echojournal.echos.domain.datasource.Mood
import com.chan.echojournal.echos.domain.settings.SettingPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

data class DataStoreSettings(
    private val context: Context
) : SettingPreferences {

    companion object {
        private val Context.settingDataStore by preferencesDataStore(
            name = "settings_datastore"
        )
    }

    private val topicsKey = stringSetPreferencesKey("topics")
    private val moodKey = stringPreferencesKey("mood")

    override suspend fun saveDefaultTopics(topics: List<String>) {
        context.settingDataStore.edit { prefs ->
            prefs[topicsKey] = topics.toSet()
        }
    }

    override fun observeDefaultTopics(): Flow<List<String>> {
        return context
            .settingDataStore
            .data
            .map { prefs ->
                prefs[topicsKey]?.toList() ?: emptyList()
            }
            .distinctUntilChanged()
    }

    override suspend fun saveDefaultMood(mood: Mood) {
        context.settingDataStore.edit { prefs ->
            prefs[moodKey] = mood.name
        }
    }

    override fun observeDefaultMood(): Flow<Mood> {
        return context
            .settingDataStore
            .data
            .map { prefs ->
                prefs[moodKey]?.let {
                    Mood.valueOf(it)
                } ?: Mood.NEUTRAL
            }
            .distinctUntilChanged()
    }
}

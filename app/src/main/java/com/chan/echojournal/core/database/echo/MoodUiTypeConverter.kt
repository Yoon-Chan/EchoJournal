package com.chan.echojournal.core.database.echo

import androidx.room.TypeConverter
import com.chan.echojournal.echos.domain.datasource.Mood

class MoodUiTypeConverter {
    @TypeConverter
    fun fromMood(mood: Mood): String {
        return mood.name
    }

    @TypeConverter
    fun toMood(moodName: String): Mood {
        return Mood.valueOf(moodName)
    }
}
package com.chan.echojournal.core.database.echo

import androidx.room.TypeConverter
import com.chan.echojournal.echos.presentation.models.MoodUI

class MoodUiTypeConverter {
    @TypeConverter
    fun fromMood(moodUi: MoodUI): String {
        return moodUi.name
    }

    @TypeConverter
    fun toMood(moodName: String): MoodUI {
        return MoodUI.valueOf(moodName)
    }
}
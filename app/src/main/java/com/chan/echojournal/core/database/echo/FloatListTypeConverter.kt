package com.chan.echojournal.core.database.echo

import androidx.room.TypeConverter

class FloatListTypeConverter {
    @TypeConverter
    fun fromList(value: List<Float>): String {
        return value.joinToString(",")
    }

    @TypeConverter
    fun toList(value: String): List<Float> {
        return value.split(",").map { it.toFloat() }
    }
}
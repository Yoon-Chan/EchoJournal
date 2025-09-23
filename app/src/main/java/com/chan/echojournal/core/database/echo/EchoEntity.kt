package com.chan.echojournal.core.database.echo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.chan.echojournal.echos.presentation.models.MoodUI

@Entity
data class EchoEntity(
    @PrimaryKey(autoGenerate = true)
    val echoId: Int = 0,
    val title: String,
    val mood: MoodUI,
    val recordedAt: Long,
    val note: String?,
    val audioFilePath: String,
    val audioPlaybackLength: Long,
    val audioAmplitudes: List<Float>
)

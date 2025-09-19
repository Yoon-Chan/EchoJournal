package com.chan.echojournal.echos.di

import com.chan.echojournal.echos.data.recording.AndroidVoiceRecorder
import com.chan.echojournal.echos.data.recording.InternalRecordingStorage
import com.chan.echojournal.echos.domain.recording.RecordingStorage
import com.chan.echojournal.echos.domain.recording.VoiceRecorder
import com.chan.echojournal.echos.presentation.create_echo.CreateEchoViewModel
import com.chan.echojournal.echos.presentation.echos.EchosViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

val echoModule = module {
    single {
        AndroidVoiceRecorder(
            context = androidApplication(),
            applicationScope = get<CoroutineScope>()
        )
    } bind VoiceRecorder::class

    singleOf(::InternalRecordingStorage).bind(RecordingStorage::class)

    viewModelOf(::EchosViewModel)
    viewModelOf(::CreateEchoViewModel)
}
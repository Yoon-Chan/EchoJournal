package com.chan.echojournal.echos.di

import com.chan.echojournal.echos.data.audio.AndroidAudioPlayer
import com.chan.echojournal.echos.data.echo.RoomEchoDataSource
import com.chan.echojournal.echos.data.recording.AndroidVoiceRecorder
import com.chan.echojournal.echos.data.recording.InternalRecordingStorage
import com.chan.echojournal.echos.data.settings.DataStoreSettings
import com.chan.echojournal.echos.domain.audio.AudioPlayer
import com.chan.echojournal.echos.domain.datasource.EchoDataSource
import com.chan.echojournal.echos.domain.recording.RecordingStorage
import com.chan.echojournal.echos.domain.recording.VoiceRecorder
import com.chan.echojournal.echos.domain.settings.SettingPreferences
import com.chan.echojournal.echos.presentation.create_echo.CreateEchoViewModel
import com.chan.echojournal.echos.presentation.echos.EchosViewModel
import com.chan.echojournal.echos.presentation.settings.SettingsViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val echoModule = module {
    single {
        AndroidVoiceRecorder(
            context = androidApplication(),
            applicationScope = get<CoroutineScope>()
        )
    } bind VoiceRecorder::class

    singleOf(::AndroidAudioPlayer) bind AudioPlayer::class
    singleOf(::RoomEchoDataSource) bind EchoDataSource::class
    singleOf(::InternalRecordingStorage).bind(RecordingStorage::class)
    singleOf(::DataStoreSettings).bind(SettingPreferences::class)

    viewModelOf(::EchosViewModel)
    viewModelOf(::CreateEchoViewModel)
    viewModelOf(::SettingsViewModel)
}
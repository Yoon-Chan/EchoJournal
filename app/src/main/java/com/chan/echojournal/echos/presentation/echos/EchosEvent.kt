package com.chan.echojournal.echos.presentation.echos

interface EchosEvent {
    data object RequestAudioPermission: EchosEvent
    data object RecordingTooShort: EchosEvent
    data object OnDoneRecording: EchosEvent
}
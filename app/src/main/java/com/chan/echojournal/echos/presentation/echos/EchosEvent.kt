package com.chan.echojournal.echos.presentation.echos

import com.chan.echojournal.echos.domain.recording.RecordingDetails

interface EchosEvent {
    data object RequestAudioPermission: EchosEvent
    data object RecordingTooShort: EchosEvent
    data class OnDoneRecording(val details: RecordingDetails): EchosEvent
}
package com.chan.echojournal.echos.presentation.create_echo

sealed interface CreateEchoEvent {
    data object FailedToSaveFile: CreateEchoEvent
    data object EchoSuccessfullySaved: CreateEchoEvent
}
package com.chan.echojournal.app.di

import com.chan.echojournal.app.EchoJournalApp
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
    single<CoroutineScope> {
        (androidApplication() as EchoJournalApp).applicationScope
    }
}
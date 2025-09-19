package com.chan.echojournal.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.chan.echojournal.app.navigation.NavigationRoot
import com.chan.echojournal.core.presentation.designystem.theme.EchoJournalTheme
import com.chan.echojournal.echos.presentation.echos.EchosRoot

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EchoJournalTheme {
                NavigationRoot(navController = rememberNavController())
            }
        }
    }
}
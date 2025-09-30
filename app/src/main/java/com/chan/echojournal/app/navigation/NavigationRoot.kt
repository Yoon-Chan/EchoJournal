package com.chan.echojournal.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.chan.echojournal.core.presentation.util.tooCreateEchoRoute
import com.chan.echojournal.echos.presentation.create_echo.CreateEchoRoot
import com.chan.echojournal.echos.presentation.echos.EchosRoot
import com.chan.echojournal.echos.presentation.settings.SettingsRoot

const val ACTION_CREATE_ECHO = "com.chan.CREATE_ECHO"

@Composable
fun NavigationRoot(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.Echos(
            startRecording = false
        )
    ) {
        composable<NavigationRoute.Echos>(
            deepLinks = listOf(
                navDeepLink<NavigationRoute.Echos>(
                    basePath = "https://echojournal.com/echos"
                ) {
                    action = ACTION_CREATE_ECHO
                }
            )
        ) {
            EchosRoot(
                onNavigateToCreateEcho = {
                    navController.navigate(
                        it.tooCreateEchoRoute()
                    )
                },
                onNavigateToSettings = {
                    navController.navigate(NavigationRoute.Settings)
                }
            )
        }

        composable<NavigationRoute.CreateEcho> {
            CreateEchoRoot(
                onConfirmLeave = {
                    navController.navigateUp()
                }
            )
        }

        composable<NavigationRoute.Settings> {
            SettingsRoot(
                onGoBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}
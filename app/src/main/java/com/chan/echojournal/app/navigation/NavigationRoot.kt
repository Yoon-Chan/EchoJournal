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
import com.chan.echojournal.core.presentation.util.tooCreateEchoRoute
import com.chan.echojournal.echos.presentation.create_echo.CreateEchoRoot
import com.chan.echojournal.echos.presentation.echos.EchosRoot

@Composable
fun NavigationRoot(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.Echos
    ) {
        composable<NavigationRoute.Echos> {
            EchosRoot(
                onNavigateToCreateEcho = {
                    navController.navigate(
                        it.tooCreateEchoRoute()
                    )
                }
            )
        }

        composable<NavigationRoute.CreateEcho> {
            CreateEchoRoot()
        }
    }
}
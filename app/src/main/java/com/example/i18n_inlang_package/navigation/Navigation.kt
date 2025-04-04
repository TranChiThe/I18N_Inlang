package com.example.i18n_inlang_package.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.i18n_inlang_package.screen.HomeScreen
import com.example.i18n_inlang_package.screen.SettingsScreen
import com.example.i18n_inlang_package.viewModel.LanguageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(viewModel: LanguageViewModel) {
    val navController = rememberNavController()
    val isLanguageLoaded = viewModel.isLanguageLoaded.collectAsState().value
    if (isLanguageLoaded) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("") },
                    actions = {
                        Button(onClick = { navController.navigate("Home") }) {
                            Text("Home")
                        }
                        Button(onClick = { navController.navigate("Setting") }) {
                            Text("Setting")
                        }
                    },
                )
            },
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = "Home",
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
            ) {
                composable("Home") { HomeScreen(viewModel) }
                composable("Setting") { SettingsScreen(viewModel) }
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
    }
}

package com.example.i18n_inlang_package.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.i18n_inlang_package.viewModel.LanguageViewModel

@Composable
fun HomeScreen(viewModel: LanguageViewModel) {
    val currentLang by viewModel.currentLanguage.collectAsState()
    val context = LocalContext.current

    val greeting = remember(currentLang) { viewModel.getString("common.greeting") }
    val welcomeMessage = remember(currentLang) { viewModel.getString("app.welcome_message") }
    val switchLanguage = remember(currentLang) { viewModel.getString("common.switch_language") }
    val settingsTitle = remember(currentLang) { viewModel.getString("settings.title") }

    LaunchedEffect(currentLang) {
        Toast.makeText(context, "Language changed to: $currentLang", Toast.LENGTH_SHORT).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = greeting,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = welcomeMessage,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = {
            val newLang = if (currentLang == "vi") "en" else "vi"
            viewModel.setLanguage(newLang)
        }) {
            Text(text = switchLanguage)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { }) {
            Text(text = settingsTitle)
        }
    }
}
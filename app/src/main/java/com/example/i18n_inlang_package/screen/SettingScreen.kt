package com.example.i18n_inlang_package.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.i18n_inlang_package.viewModel.LanguageViewModel

@Composable
fun SettingsScreen(viewModel: LanguageViewModel) {
    val currentLang by viewModel.currentLanguage.collectAsState()
    val switchLanguage = remember(currentLang) { viewModel.getString("common.switch_language") }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = viewModel.getString("settings.title"),
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = viewModel.getString("settings.language"),
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = {
            val newLang = if (currentLang == "vi") "en" else "vi"
            viewModel.setLanguage(newLang)
        }) {
            Text(text = switchLanguage)
        }
    }
}

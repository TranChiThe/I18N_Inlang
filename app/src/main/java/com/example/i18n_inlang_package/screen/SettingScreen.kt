package com.example.i18n_inlang_package.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.i18n_inlang_package.viewModel.LanguageViewModel

@Composable
fun SettingsScreen(viewModel: LanguageViewModel) {
    val currentLang by viewModel.currentLanguage.collectAsState()
    val switchLanguage = remember(currentLang) { viewModel.getString("common.switch_language") }
    val languageList = viewModel.getAvailableLanguages()
    var expanded by remember { mutableStateOf(false) }

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
        Box {
            Button(
                onClick = { expanded = true },
                colors = ButtonDefaults.buttonColors(Color(0xFF3399FF)),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = viewModel.getFlagResource(currentLang)),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = viewModel.getLanguageName(currentLang))
                }
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                languageList.forEach { (langCode, langName, flagRes) ->
                    DropdownMenuItem(text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = flagRes),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = langName)
                        }
                    }, onClick = {
                        viewModel.setLanguage(langCode)
                        expanded = false
                    })
                }
            }
        }
    }
}

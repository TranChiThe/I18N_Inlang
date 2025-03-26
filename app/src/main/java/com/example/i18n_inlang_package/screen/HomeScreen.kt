package com.example.i18n_inlang_package.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.i18n_library.LocalizationManager

@Composable
fun HomeScreen() {
    var selectedLanguage by remember { mutableStateOf("vi") }
    val context = LocalContext.current
    var localizedText by remember { mutableStateOf(LocalizationManager.getString("hello")) }
    var welcome by remember { mutableStateOf(LocalizationManager.getString("welcome")) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = localizedText)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = welcome)
        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = {
            selectedLanguage = if (selectedLanguage == "vi") "en" else "vi"
            LocalizationManager.loadLanguage(context, selectedLanguage)
            localizedText = LocalizationManager.getString("hello")
            welcome = LocalizationManager.getString("welcome")
        }) {
            Text(text = "Change Language")
        }
    }
}
package com.example.i18n_inlang_package

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.i18n_inlang_package.screen.HomeScreen
import com.example.i18n_inlang_package.ui.theme.I18N_InLang_PackageTheme
import com.example.i18n_library.LocalizationManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        LocalizationManager.loadLanguage(this, "vi")
        setContent {
            I18N_InLang_PackageTheme {
                HomeScreen()
            }
        }
    }
}


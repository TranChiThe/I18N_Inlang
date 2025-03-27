package com.example.i18n_inlang_package

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.i18n_inlang_package.screen.HomeScreen
import com.example.i18n_inlang_package.screen.SettingsScreen
import com.example.i18n_inlang_package.ui.theme.I18N_InLang_PackageTheme
import com.example.i18n_inlang_package.viewModel.LanguageViewModel
import com.example.i18n_library.LanguageUtil

class MainActivity : ComponentActivity() {
    private val languageViewModel: LanguageViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        LanguageUtil.applyLanguage(this)
        setContent {
            I18N_InLang_PackageTheme {
                MyApp(languageViewModel)
            }
        }
    }
}
@Composable
fun MyApp(viewModel: LanguageViewModel) {
    val isLanguageLoaded by viewModel.isLanguageLoaded.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (isLanguageLoaded) {
            HomeScreen(viewModel)
        } else {
            CircularProgressIndicator(modifier = Modifier.size(100.dp).padding(top = 50.dp))
        }
    }
}
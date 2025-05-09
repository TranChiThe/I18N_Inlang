package com.example.i18n_inlang_package

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.i18n_inlang_package.navigation.Navigation
import com.example.i18n_inlang_package.ui.theme.I18N_InLang_PackageTheme
import com.example.i18n_library.LanguageUtil
import com.example.sample_app.viewModel.LanguageViewModel

class MainActivity : ComponentActivity() {
    private val languageViewModel: LanguageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        LanguageUtil.applyLanguage(this)
        setContent {
            I18N_InLang_PackageTheme {
                Navigation(viewModel = languageViewModel)
            }
        }
    }
}

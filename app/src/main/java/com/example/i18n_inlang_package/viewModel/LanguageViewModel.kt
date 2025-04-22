package com.example.sample_app.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.i18n_inlang_package.R
import com.example.i18n_library.I18nManager
import com.example.i18n_library.LanguageUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LanguageViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val _currentLanguage = MutableStateFlow(LanguageUtil.getSavedLanguage(application))
    val currentLanguage: StateFlow<String> = _currentLanguage

    private val i18nManager = I18nManager.getInstance(application)
    private val _isLanguageLoaded = MutableStateFlow(false)
    val isLanguageLoaded: StateFlow<Boolean> = _isLanguageLoaded

    private val _version = MutableStateFlow("123")
    val version: StateFlow<String> = _version

    init {
        viewModelScope.launch {
            updateLanguageLoadedState(_currentLanguage.value)
        }
    }

    fun setLanguage(
        language: String,
        recreateActivity: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {
            _isLanguageLoaded.value = false
            LanguageUtil.setLocale(getApplication(), language)
            i18nManager.setLanguage(language)
            _currentLanguage.value = language
            updateLanguageLoadedState(language)
            recreateActivity?.invoke()
        }
    }

    private suspend fun updateLanguageLoadedState(language: String) {
        while (!i18nManager.isLanguageLoaded(language)) {
            kotlinx.coroutines.delay(100)
        }
        _isLanguageLoaded.value = true
    }

    fun getString(
        key: String,
        params: Map<String, String> = emptyMap(),
    ): String = i18nManager.getString(key, params)

    fun isLanguageLoaded(): Boolean = i18nManager.isLanguageLoaded(currentLanguage.value)

    fun getAvailableLanguages(): List<Triple<String, String, Int>> =
        listOf(
            Triple("en", "English", R.drawable.usa_flag),
            Triple("id", "Indonesia", R.drawable.indonesia),
            Triple("vi", "Việt Nam", R.drawable.vietnam_flag),
        )

    fun getFlagResource(langCode: String): Int =
        when (langCode) {
            "en" -> R.drawable.usa_flag
            "id" -> R.drawable.indonesia
            "vi" -> R.drawable.vietnam_flag
            else -> R.drawable.ic_launcher_foreground
        }

    fun getLanguageName(currentLang: String): String =
        when (currentLang) {
            "en" -> "English"
            "id" -> "Indonesia"
            "vi" -> "Việt Nam"
            else -> "Not found"
        }
}

package com.example.i18n_inlang_package.viewModel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.i18n_library.I18nManager
import com.example.i18n_library.LanguageUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LanguageViewModel(application: Application) : AndroidViewModel(application) {
    private val _currentLanguage = MutableStateFlow(LanguageUtil.getSavedLanguage(application))
    val currentLanguage: StateFlow<String> = _currentLanguage

    private val i18nManager = I18nManager.getInstance(application)
    private val _isLanguageLoaded = MutableStateFlow(false)
    val isLanguageLoaded: StateFlow<Boolean> = _isLanguageLoaded

    init {
        viewModelScope.launch {
            while (!i18nManager.isLanguageLoaded(_currentLanguage.value)) {
                delay(100)
            }
            _isLanguageLoaded.value = true
        }
    }

    fun setLanguage(language: String) {
        viewModelScope.launch {
            LanguageUtil.setLocale(getApplication(), language)
            i18nManager.setLanguage(language)
            _currentLanguage.value = language
            Toast.makeText(getApplication(), "ViewModel set language to: $language", Toast.LENGTH_SHORT).show()
        }
    }

    fun getString(key: String): String = i18nManager.getString(key)
    fun isLanguageLoaded(): Boolean = i18nManager.isLanguageLoaded(currentLanguage.value)
}
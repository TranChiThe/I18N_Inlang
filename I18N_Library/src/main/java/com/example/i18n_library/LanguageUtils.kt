package com.example.i18n_library

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import java.util.Locale

object LanguageUtil {
    private const val PREFS_NAME = "LanguagePrefs"
    private const val KEY_LANGUAGE = "language"
    private val SUPPORTED_LANGUAGES = setOf("en", "id", "vi")

    fun setLocale(
        context: Context,
        language: String,
    ): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config =
            Configuration(context.resources.configuration).apply {
                setLocale(locale)
            }

        context
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_LANGUAGE, language)
            .apply()
        return context.createConfigurationContext(config)
    }

    fun getSavedLanguage(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        if (!prefs.contains(KEY_LANGUAGE)) {
            val systemLanguage = getSystemLanguage()
            val defaultLanguage =
                if (SUPPORTED_LANGUAGES.contains(systemLanguage)) {
                    systemLanguage
                } else {
                    "en"
                }
            prefs.edit().putString(KEY_LANGUAGE, defaultLanguage).apply()
            return defaultLanguage
        }
        return prefs.getString(KEY_LANGUAGE, "en") ?: "en"
    }

    fun applyLanguage(context: Context) {
        setLocale(context, getSavedLanguage(context))
    }

    fun getSystemLanguage(): String {
        val locale = Locale.getDefault()
        val language = locale.language
        Log.d("LanguageUtil", "System locale: $locale, language: $language")
        return when {
            language.startsWith("in") -> "id"
            SUPPORTED_LANGUAGES.contains(language) -> language
            else -> "en"
        }
    }
}

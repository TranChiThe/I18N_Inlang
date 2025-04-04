@file:Suppress("ktlint:standard:filename")

package com.example.i18n_library

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LanguageUtil {
    private const val PREFS_NAME = "LanguagePrefs"
    private const val KEY_LANGUAGE = "language"
    private val SUPPORTED_LANGUAGES = setOf("en", "vi")

    fun setLocale(
        context: Context,
        language: String,
    ) {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config =
            Configuration().apply {
                setLocale(locale)
            }
        context.resources.updateConfiguration(config, context.resources.displayMetrics)

        context
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_LANGUAGE, language)
            .apply()
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

    fun getSystemLanguage(): String = Locale.getDefault().language
}

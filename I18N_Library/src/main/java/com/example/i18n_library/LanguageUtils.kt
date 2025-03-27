package com.example.i18n_library

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import java.util.*

object LanguageUtil {
    private const val PREFS_NAME = "LanguagePrefs"
    private const val KEY_LANGUAGE = "language"

    fun setLocale(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = Configuration().apply {
            setLocale(locale)
        }
        context.resources.updateConfiguration(config, context.resources.displayMetrics)

        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_LANGUAGE, language)
            .apply()

        I18nManager.getInstance(context).setLanguage(language)
    }

    fun getSavedLanguage(context: Context): String {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_LANGUAGE, "vi") ?: "vi"
    }

    fun applyLanguage(context: Context) {
        setLocale(context, getSavedLanguage(context))
    }
}
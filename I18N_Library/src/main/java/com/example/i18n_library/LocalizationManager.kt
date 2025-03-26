package com.example.i18n_library

import android.content.Context
import org.json.JSONObject
import java.io.BufferedReader

object LocalizationManager {
    private var translations: JSONObject? = null

    fun loadLanguage(context: Context, languageCode: String) {
        val resId = when (languageCode) {
            "vi" -> R.raw.locales_vi
            else -> R.raw.locales_en
        }
        val jsonString =
            context.resources.openRawResource(resId).bufferedReader().use(BufferedReader::readText)
        translations = JSONObject(jsonString)
    }

    fun getString(key: String): String {
        return translations?.optString(key) ?: key
    }
}

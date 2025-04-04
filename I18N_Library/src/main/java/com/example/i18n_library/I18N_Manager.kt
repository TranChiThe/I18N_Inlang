package com.example.i18n_library

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

class I18nManager private constructor(
    context: Context,
) {
    private val languageMaps = mutableMapOf<String, JSONObject>()
    private var currentLanguage = LanguageUtil.getSavedLanguage(context)
    private val appContext = context.applicationContext
    private val scope = CoroutineScope(Dispatchers.Main)

    init {
        loadLanguagesAsync()
    }

    companion object {
        @Volatile
        private var instance: I18nManager? = null

        fun getInstance(context: Context): I18nManager =
            instance ?: synchronized(this) {
                instance ?: I18nManager(context).also { instance = it }
            }
    }

    private fun loadLanguagesAsync() {
        scope.launch {
            val languages = arrayOf("en", "vi")
            val assetManager = appContext.assets
            languages.forEach { lang ->
                try {
                    val jsonString =
                        withContext(Dispatchers.IO) {
                            assetManager.open("lang/$lang.json").use { inputStream ->
                                BufferedReader(InputStreamReader(inputStream)).readText()
                            }
                        }
                    languageMaps[lang] = JSONObject(jsonString)
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.d("Error", "Failed to load $lang: ${e.message}")
                    }
                }
            }
            withContext(Dispatchers.Main) {
                val savedLang = LanguageUtil.getSavedLanguage(appContext)
                if (currentLanguage != savedLang) {
                    currentLanguage = savedLang
                }
            }
        }
    }

    fun setLanguage(language: String) {
        currentLanguage = language
    }

    fun getString(key: String): String {
        val jsonObject = languageMaps[currentLanguage]
        return try {
            if (jsonObject == null) return key
            val keys = key.split(".")
            var currentObject: JSONObject? = jsonObject
            for (i in 0 until keys.size - 1) {
                currentObject = currentObject?.optJSONObject(keys[i])
                if (currentObject == null) return key
            }
            currentObject?.getString(keys.last()) ?: key
        } catch (e: Exception) {
            key
        }
    }

    fun getCurrentLanguage(): String = currentLanguage

    fun isLanguageLoaded(lang: String): Boolean = languageMaps.containsKey(lang)
}

package com.example.i18n_library

import android.content.Context
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

class I18nManager private constructor(context: Context) {
    private val languageMaps = mutableMapOf<String, JSONObject>()
    private var currentLanguage = "vi"
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
                    val jsonString = withContext(Dispatchers.IO) {
                        assetManager.open("lang/$lang.json").use { inputStream ->
                            BufferedReader(InputStreamReader(inputStream)).readText()
                        }
                    }
                    languageMaps[lang] = JSONObject(jsonString)
                } catch (e: Exception) {
                    Log.e("I18nManager", "Error loading language file: $lang", e)
                }
            }
            if (!languageMaps.containsKey(currentLanguage)) {
                currentLanguage = "vi"
                LanguageUtil.setLocale(appContext, "vi")
            }
        }
    }

    fun setLanguage(language: String) {
        if (languageMaps.containsKey(language)) {
            currentLanguage = language
            Toast.makeText(appContext, "Switched to $language", Toast.LENGTH_SHORT).show()
        }
    }


    fun getString(key: String): String {
        return try {
            val jsonObject = languageMaps[currentLanguage] ?: return key
            val keys = key.split(".")
            var currentObject: JSONObject? = jsonObject
            for (i in 0 until keys.size - 1) {
                currentObject = currentObject?.optJSONObject(keys[i])
                if (currentObject == null) return key
            }
            val result = currentObject?.getString(keys.last()) ?: key
            result
        } catch (e: Exception) {
            key
        }
    }

    fun getCurrentLanguage(): String = currentLanguage

    fun isLanguageLoaded(lang: String): Boolean = languageMaps.containsKey(lang)
}
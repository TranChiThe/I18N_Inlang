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

    // (lấy từ inlang.config.json)
    private val namespaces =
        mapOf(
            "common" to "translations/{lang}/ui_and_ux/common.json",
            "mediaMessages" to "translations/{lang}/chat/attachments/mediaMessages.json",
            "editor" to "translations/{lang}/chat/editor/editor.json",
            "updateManager" to "translations/{lang}/app/updateManager.json",
            "mutualChannels" to "translations/{lang}/social/friend_suggests/mutualChannels.json",
            "mutualFriends" to "translations/{lang}/social/friend_suggests/mutualFriends.json",
        )

    init {
        loadLanguageAsync(currentLanguage)
    }

    companion object {
        @Volatile
        private var instance: I18nManager? = null

        fun getInstance(context: Context): I18nManager =
            instance ?: synchronized(this) {
                instance ?: I18nManager(context).also { instance = it }
            }
    }

    private fun loadLanguageAsync(lang: String) {
        if (languageMaps.containsKey(lang)) return

        scope.launch {
            try {
                val langObject = JSONObject()
                val assetManager = appContext.assets
                namespaces.forEach { (namespace, pathTemplate) ->
                    val filePath = pathTemplate.replace("{lang}", lang)
                    try {
                        val jsonString =
                            withContext(Dispatchers.IO) {
                                assetManager.open(filePath).use { inputStream ->
                                    BufferedReader(InputStreamReader(inputStream)).readText()
                                }
                            }
                        langObject.put(namespace, JSONObject(jsonString))
                    } catch (e: Exception) {
                        Log.e("I18nManager", "Failed to load $filePath: ${e.message}")
                    }
                }
                languageMaps[lang] = langObject
                withContext(Dispatchers.Main) {
                    if (lang == currentLanguage) {
                        Log.d("I18nManager", "Loaded language: $lang")
                    }
                }
            } catch (e: Exception) {
                Log.e("I18nManager", "Failed to load language $lang: ${e.message}")
            }
        }
    }

    fun setLanguage(language: String) {
        if (currentLanguage != language) {
            currentLanguage = language
            loadLanguageAsync(language)
        }
    }

    fun getString(key: String): String = getString(key, emptyMap())

    fun getString(
        key: String,
        params: Map<String, String>,
    ): String {
        val jsonObject = languageMaps[currentLanguage]
        return try {
            if (jsonObject == null) return key
            val keys = key.split(".")
            var currentObject: JSONObject? = jsonObject
            for (i in 0 until keys.size - 1) {
                currentObject = currentObject?.optJSONObject(keys[i])
                if (currentObject == null) return key
            }
            var result = currentObject?.getString(keys.last()) ?: key
            params.forEach { (param, value) ->
                result = result.replace("<$param>", value)
            }
            result
        } catch (e: Exception) {
            Log.e("I18nManager", "Error retrieving key $key: ${e.message}")
            key
        }
    }

    fun getCurrentLanguage(): String = currentLanguage

    fun isLanguageLoaded(lang: String): Boolean = languageMaps.containsKey(lang)
}

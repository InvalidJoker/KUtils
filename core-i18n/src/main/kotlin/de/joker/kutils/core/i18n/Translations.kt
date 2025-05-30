package de.joker.kutils.core.i18n

import de.joker.kutils.core.i18n.interfaces.TranslationHook
import de.joker.kutils.core.i18n.interfaces.TranslationSource
import de.joker.kutils.core.i18n.sources.JsonTranslationSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

object Translations {

    lateinit var manager: TranslationManager
    private val translationHooks = arrayListOf<TranslationHook>()

    fun registerTranslationHook(hook: TranslationHook) {
        translationHooks.add(hook)
    }

    val defaultTranslationSource by lazy {
        JsonTranslationSource(File("translations"))
    }

    fun getTranslation(language: String, key: String, placeholders: Map<String, Any?> = mapOf()): String? {
        if (!Translations::manager.isInitialized) return null

        var translation = manager.get(language, key, placeholders)?.message ?: return null

        for (hook in translationHooks) {
            translation = hook.onHandleTranslation(language, key, placeholders, translation)
        }

        return translation
    }

    val translations get() = manager

    fun load(
        source: TranslationSource = defaultTranslationSource,
        writeNotFound: Boolean = true,
        callback: ((Map<String, Int>) -> Unit)? = null
    ) {
        manager = TranslationManager(source, writeNotFound)

        CoroutineScope(Dispatchers.Default).launch {
            manager.loadTranslations(callback)
        }
    }
}
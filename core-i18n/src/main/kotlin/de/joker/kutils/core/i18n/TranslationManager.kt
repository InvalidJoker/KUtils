package de.joker.kutils.core.i18n

import com.github.benmanes.caffeine.cache.Caffeine
import de.joker.kutils.core.extensions.getLogger
import de.joker.kutils.core.i18n.interfaces.TranslationSource
import de.joker.kutils.core.tools.TempStorage
import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap

class TranslationManager(
    private val source: TranslationSource,
    private val writeFailedTranslations: Boolean = false
) {
    private val fallbackLanguage = "en_US"

    private val cache = Caffeine.newBuilder()
        .build<String, List<Translation>>()

    private val notFoundTranslations = ConcurrentHashMap.newKeySet<String>()

    suspend fun loadTranslations(callback: ((Map<String, Int>) -> Unit)? = null) {
        val allLanguages = source.getLanguages()
        coroutineScope {
            allLanguages.map { languageCode ->
                async {
                    val translations = source.getTranslations(languageCode).map {
                        Translation(languageCode, it.messageKey, it.message)
                    }
                    cache.put(languageCode, translations)
                }
            }.awaitAll()
        }
        callback?.invoke(getAll())
    }

    fun getAll(): Map<String, Int> =
        cache.asMap().mapValues { it.value.size }

    fun getAllTranslations(): Map<String, List<Translation>> =
        cache.asMap()

    fun get(languageCode: String, messageKey: String, placeholders: Map<String, Any?> = emptyMap()): Translation? {
        val message = cache.getIfPresent(languageCode)?.find { it.messageKey == messageKey }
            ?: cache.getIfPresent(fallbackLanguage)?.find { it.messageKey == messageKey }

        if (message == null) {
            getLogger().info("No translation found for $languageCode:$messageKey")
            if (languageCode != fallbackLanguage) return null
            notFoundTranslations.add(
                "$messageKey||" + placeholders.entries.joinToString("|") {
                    "${it.key}::${it.value?.javaClass?.simpleName ?: "null"}"
                }
            )
            writeFailedTranslations()
            return null
        }

        val replaced = placeholders.entries.fold(message.message) { acc, (key, value) ->
            acc.replace("%$key%", value.toString())
        }

        return message.copy(message = replaced)
    }

    private fun writeFailedTranslations() {
        if (notFoundTranslations.isEmpty() || !writeFailedTranslations) return
        TempStorage.saveTempFile("not-found-translations.txt", notFoundTranslations.joinToString("\n"))
    }

    fun contains(languageCode: String): Boolean =
        cache.getIfPresent(languageCode) != null

    fun addTranslation(languageCode: String, messageKey: String, message: String) {
        val newTranslation = Translation(languageCode, messageKey, message)
        val updated = (cache.getIfPresent(languageCode) ?: emptyList()) + newTranslation
        cache.put(languageCode, updated)

        CoroutineScope(Dispatchers.Default).launch {
            source.addTranslation(languageCode, messageKey, message)
        }
    }
}

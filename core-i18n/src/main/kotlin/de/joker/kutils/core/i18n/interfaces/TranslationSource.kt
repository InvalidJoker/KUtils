package de.joker.kutils.core.i18n.interfaces

import de.joker.kutils.core.i18n.Translation

interface TranslationSource {
    suspend fun getLanguages(): List<String>

    suspend fun getTranslations(language: String): List<Translation>

    suspend fun addTranslation(
        languageCode: String,
        messageKey: String,
        message: String
    )
}
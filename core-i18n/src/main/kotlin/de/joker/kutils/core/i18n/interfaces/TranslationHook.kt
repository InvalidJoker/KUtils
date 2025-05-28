package de.joker.kutils.core.i18n.interfaces

fun interface TranslationHook {

    fun onHandleTranslation(language: String, key: String, placeholders: Map<String, Any?>, result: String): String

}
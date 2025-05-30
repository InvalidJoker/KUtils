package de.joker.kutils.core.i18n.sources

import de.joker.kutils.core.i18n.Translation
import de.joker.kutils.core.i18n.interfaces.TranslationSource
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class JsonTranslationSource(private val directory: File) : TranslationSource {
    override suspend fun getLanguages(): List<String> {
        if (!directory.exists()) {
            directory.mkdirs()
        }
        return directory.listFiles { file -> file.extension == "json" }
            ?.map { it.nameWithoutExtension }
            ?: emptyList()
    }

    override suspend fun getTranslations(language: String): List<Translation> {
        val langFile = File(directory, "$language.json")
        if (!langFile.exists()) return emptyList()

        val content = langFile.readText()
        val data: Map<String, String> = Json.decodeFromString(content)

        return data.map { (key, value) ->
            Translation(
                language,
                key,
                value
            )
        }
    }

    override suspend fun addTranslation(languageCode: String, messageKey: String, message: String) {
        val langFile = File(directory, "$languageCode.json")
        if (!langFile.exists()) {
            langFile.createNewFile()
            langFile.writeText("{}") // Initialize with an empty JSON object
        }

        val content = langFile.readText()
        val data: Map<String, String> = Json.decodeFromString(content)

        val mutableData = data.toMutableMap()

        mutableData[messageKey] = message

        val out = Json.encodeToString(mutableData)

        langFile.writeText(out)
    }
}

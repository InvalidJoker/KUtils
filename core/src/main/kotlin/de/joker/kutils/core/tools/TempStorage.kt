package de.joker.kutils.core.tools

import dev.fruxz.ascend.json.globalJson
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.io.File

/**
 * A utility class for storing and retrieving temporary files, including serialization support.
 */
open class BaseTempStorage(
    dir: String = ".temp",
    private val json: Json = globalJson
) {
    private val tempFolder = File(dir).apply { mkdirs() }

    /**
     * Saves a temporary file with the given name and byte content.
     */
    fun saveTempFile(name: String, content: ByteArray) {
        val file = File(tempFolder, name)
        file.ensureParentExists()
        file.writeBytes(content)
    }

    /**
     * Saves a temporary file with the given name and text content.
     */
    fun saveTempFile(name: String, content: String) {
        val file = File(tempFolder, name)
        file.ensureParentExists()
        file.writeText(content)
    }

    /**
     * Saves a temporary file by copying another file.
     */
    fun saveTempFile(name: String, content: File) {
        val target = File(tempFolder, name)
        target.ensureParentExists()
        content.copyTo(target, overwrite = true)
    }

    /**
     * Saves a serializable object into a temp file as JSON.
     */
    fun <T> saveTempFile(name: String, data: T, serializer: KSerializer<T>) {
        val jsonString = json.encodeToString(serializer, data)
        saveTempFile(name, jsonString)
    }

    /**
     * Deletes a temp file.
     */
    fun deleteTempFile(name: String): Boolean {
        return File(tempFolder, name).delete()
    }

    /**
     * Reads a temp file as bytes.
     */
    fun readTempFile(name: String): ByteArray {
        val file = File(tempFolder, name)
        if (!file.exists()) error("Temp file $name does not exist.")
        return file.readBytes()
    }

    /**
     * Reads a temp file as text.
     */
    fun readTempFileAsString(name: String): String {
        val file = File(tempFolder, name)
        if (!file.exists()) error("Temp file $name does not exist.")
        return file.readText()
    }

    /**
     * Reads a temp file as text, or returns null if missing.
     */
    fun readTempFileAsStringOrNull(name: String): String? {
        val file = File(tempFolder, name)
        return if (file.exists()) file.readText() else null
    }

    /**
     * Reads a temp file as a deserialized object from JSON.
     */
    fun <T> readTempFileAs(name: String, serializer: KSerializer<T>): T {
        val text = readTempFileAsString(name)
        return json.decodeFromString(serializer, text)
    }

    /**
     * Retrieves a temp file by name.
     */
    fun getTempFile(name: String): File {
        return File(tempFolder, name)
    }

    /**
     * Lists files in a subfolder of the temp directory.
     */
    fun getTempFiles(path: String): List<File> {
        val folder = File(tempFolder, path).apply { mkdirs() }
        return folder.listFiles()?.toList() ?: emptyList()
    }

    /**
     * Ensures parent folders exist for a file.
     */
    private fun File.ensureParentExists() {
        parentFile?.mkdirs()
    }
}

/**
 * Singleton instance of the default temp storage.
 */
object TempStorage : BaseTempStorage()
package de.joker.kutils.paper.file.base

import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException

/**
 * Represents a configuration file for storing settings in YAML format.
 *
 * This class extends [org.bukkit.configuration.file.YamlConfiguration] and provides additional functionality for handling file operations and
 * loading/saving configuration data.
 *
 * @property path The name of the configuration file.
 */
abstract class FileYamlConfig(var path: String) : YamlConfiguration() {
    private var seperator: String?

    val file: File

    fun saveConfig() {
        try {
            save(path)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun deleteConfig() {
        file.delete()
    }

    init {
        seperator = System.getProperty("file.seperator")
        if (seperator == null) {
            seperator = "/"
        }
        path = path.replace("/", seperator.toString())
        file = File(path)
        try {
            if (!file.exists()) {
                file.createNewFile()
            }
            load(path)
        } catch (_: IOException) {
            // Do nothing
        } catch (e: InvalidConfigurationException) {
            e.printStackTrace()
        }
    }
}
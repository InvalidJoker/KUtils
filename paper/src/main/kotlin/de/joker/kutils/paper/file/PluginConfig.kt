package de.joker.kutils.paper.file

import de.joker.kutils.paper.PluginInstance
import de.joker.kutils.paper.file.base.FileYamlConfig

/**
 * Represents a configuration file for storing settings in YAML format.
 *
 * This class extends [org.bukkit.configuration.file.YamlConfiguration] and provides additional functionality for handling file operations and
 * loading/saving configuration data.
 *
 * @property fileName The name of the configuration file.
 */
class PluginConfig(fileName: String) : FileYamlConfig("plugins/${PluginInstance.dataFolder.name}/$fileName")

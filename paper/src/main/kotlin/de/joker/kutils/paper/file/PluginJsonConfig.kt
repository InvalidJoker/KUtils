package de.joker.kutils.paper.file

import de.joker.kutils.paper.PluginInstance
import de.joker.kutils.paper.file.base.FileJsonConfig

/**
 * Represents a configuration file for storing settings in YAML format.
 *
 *
 * @property fileName The name of the configuration file.
 */
class PluginJsonConfig(fileName: String) : FileJsonConfig("plugins/${PluginInstance.dataFolder.name}/$fileName")

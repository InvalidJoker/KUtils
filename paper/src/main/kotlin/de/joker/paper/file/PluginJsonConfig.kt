package de.joker.paper.file

import de.joker.paper.PluginInstance
import de.joker.paper.file.base.FileJsonConfig

/**
 * Represents a configuration file for storing settings in YAML format.
 *
 *
 * @property fileName The name of the configuration file.
 */
class PluginJsonConfig(fileName: String) : FileJsonConfig("plugins/${PluginInstance.dataFolder.name}/$fileName")
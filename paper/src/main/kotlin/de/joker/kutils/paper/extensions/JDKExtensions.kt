package de.joker.kutils.paper.extensions

import de.joker.kutils.paper.PluginInstance
import org.slf4j.LoggerFactory

fun getPluginLogger(): org.slf4j.Logger {
    return LoggerFactory.getLogger(PluginInstance::class.java)
}

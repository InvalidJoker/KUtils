package de.joker.paper.extensions

import de.joker.paper.PluginInstance
import org.slf4j.LoggerFactory

fun getLogger(): org.slf4j.Logger {
    return LoggerFactory.getLogger(PluginInstance::class.java)
}
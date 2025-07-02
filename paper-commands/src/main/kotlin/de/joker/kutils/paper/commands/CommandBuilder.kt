package de.joker.kutils.paper.commands

import dev.jorel.commandapi.CommandTree

interface CommandBuilder {
    fun register(): CommandTree
}
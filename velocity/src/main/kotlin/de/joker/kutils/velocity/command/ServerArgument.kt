package de.joker.kutils.velocity.command

import com.velocitypowered.api.proxy.server.RegisteredServer
import de.joker.kutils.velocity.main.PluginInstance
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.CommandTree
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.TextArgument

open class ServerArgument(nodeName: String) : CustomArgument<RegisteredServer, String>(
    TextArgument(nodeName)
        .replaceSuggestions(
            ArgumentSuggestions.strings { info ->
                PluginInstance.server.allServers.map { it.serverInfo.name }
                    .toTypedArray()
            }
        ),
    { info ->
        val raw = info.currentInput

        val p = PluginInstance.server.getServer(raw)

        if (p.isEmpty) {
            throw CustomArgumentException.fromMessageBuilder(MessageBuilder()
                .append("Server not found: ")
                .appendArgInput()
            )
        }

        p.get()
    }
)

inline fun CommandAPICommand.serverArgument(
    nodeName: String,
    optional: Boolean = false,
    block: Argument<*>.() -> Unit = {},
): CommandAPICommand =
    withArguments(ServerArgument(nodeName).setOptional(optional).apply(block))

inline fun CommandTree.serverArgument(
    nodeName: String,
    optional: Boolean = false,
    block: Argument<*>.() -> Unit = {},
): CommandTree =
    then(ServerArgument(nodeName).setOptional(optional).apply(block))
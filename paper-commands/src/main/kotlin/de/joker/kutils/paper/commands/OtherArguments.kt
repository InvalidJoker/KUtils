package de.joker.kutils.paper.commands

import de.joker.kutils.core.extensions.ifTrue
import de.joker.kutils.paper.PluginInstance
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import dev.jorel.commandapi.executors.CommandArguments
import org.bukkit.OfflinePlayer
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.OfflinePlayerArgument
import dev.jorel.commandapi.arguments.SafeSuggestions
import org.bukkit.Bukkit
import org.bukkit.entity.Player

fun CommandAPI.load(
    silentLogs: Boolean = false,
    namespace: String? = null
) {
    CommandAPI.onLoad(CommandAPIBukkitConfig(PluginInstance)
        .silentLogs(silentLogs)
        .ifTrue({ namespace != null }) {
            it.setNamespace(namespace)
        }
    )
}

inline fun CommandAPICommand.suggestedOfflinePlayerArgument(
    nodeName: String,
    optional: Boolean = false,
    block: Argument<*>.() -> Unit = {},
): CommandAPICommand =
    withArguments(
        OfflinePlayerArgument(nodeName).replaceSafeSuggestions(
            SafeSuggestions.suggest {
                Bukkit.getOnlinePlayers().toTypedArray<Player>()
            }).setOptional(optional).apply(block))


fun <T> CommandArguments.getUncheckedOrFail(
    nodeName: String,
    message: String? = "$nodeName not found"
): T = getUnchecked<T>(nodeName) ?: throw CommandAPI.failWithString(message)

fun CommandArguments.getOfflinePlayerOrFail(nodeName: String): OfflinePlayer =
    getUncheckedOrFail(nodeName, "$nodeName is not a valid player")

fun CommandArguments.getPlayerOrFail(nodeName: String): Player =
    getUncheckedOrFail(nodeName, "$nodeName is not a valid player")
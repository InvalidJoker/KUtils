package de.joker.kutils.adventure

import dev.fruxz.stacked.text
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import kotlin.collections.forEach

fun Audience.send(message: String, tagResolver: TagResolver = TagResolver.standard()) {
    this.sendMessage(
        text(
            tagResolver = tagResolver,
            content = message
        )
    )
}

fun Audience.sendEmptyLine() = sendMessage(text(" "))

fun Collection<Audience>.send(message: String, tagResolver: TagResolver = TagResolver.standard()) {
    forEach { it.send(message, tagResolver) }
}

fun Collection<Audience>.send(message: Component) {
    forEach { it.sendMessage(message) }
}

fun Audience.sendMessageBlock(vararg lines: String) {
    sendEmptyLine()
    lines.forEach { send(it) }
    sendEmptyLine()
}

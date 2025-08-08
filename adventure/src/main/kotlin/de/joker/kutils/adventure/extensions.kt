package de.joker.kutils.adventure

import dev.fruxz.stacked.text
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import kotlin.collections.forEach

fun Audience.send(message: String) {
    this.sendMessage(text(message))
}

fun Audience.sendEmtpyLine() = sendMessage(text(" "))


fun Collection<Audience>.send(message: String) {
    forEach { it.send(message) }
}

fun Collection<Audience>.send(message: Component) {
    forEach { it.sendMessage(message) }
}

fun Audience.sendMessageBlock(vararg lines: String) {
    sendEmtpyLine()
    lines.forEach { send(it) }
    sendEmtpyLine()
}

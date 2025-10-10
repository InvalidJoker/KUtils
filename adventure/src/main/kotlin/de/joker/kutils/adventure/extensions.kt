package de.joker.kutils.adventure

import dev.fruxz.stacked.text
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextReplacementConfig
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.format.TextDecoration.State
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


/**
 * Create a [Component] with basic styles. By default, every style is deactivated. The component does not inherit the styles of previous Components
 * @param str The content of the [Component]
 * @param color The [TextColor] of the [Component]. If multiple colors are given, the component will be colored with a gradient of those colors)
 * @param bold If the [Component] should be bold
 * @param italic If the [Component] should be italic
 * @param underlined If the [Component] should be underlined
 * @param strikethrough If the [Component] should be striked through
 * @param obfuscated If the [Component] should be obfuscated (magic chars)
 * @return A [Component] with the given content with the styles applied accordingly
 */
fun text(
    str: String,
    vararg color: TextColor = arrayOf(NamedTextColor.GRAY),
    bold: Boolean = false,
    italic: Boolean = false,
    underlined: Boolean = false,
    strikethrough: Boolean = false,
    obfuscated: Boolean = false,
): Component =
    (if (color.size == 1) Component.text(str)
        .color(color.first()) else text("<gradient:${color.joinToString(":") { it.asHexString() }}>$str</gradient>"))
        .apply {
            decorations(
                mapOf(
                    TextDecoration.BOLD to State.byBoolean(bold),
                    TextDecoration.ITALIC to State.byBoolean(italic),
                    TextDecoration.UNDERLINED to State.byBoolean(underlined),
                    TextDecoration.STRIKETHROUGH to State.byBoolean(strikethrough),
                    TextDecoration.OBFUSCATED to State.byBoolean(obfuscated),
                )
            )
        }


/**
 * Replace a [String] in a [Component] with a [Component]
 * @param old The Text which should be replaced
 * @param new The Component to replace [old] with (Will keep its styling)
 * @return Itself, with all instances of [old] replaced by [new]
 * @author Max Bossing
 * @since 0.0.1
 */
fun Component.replace(old: String, new: Component) =
    replaceText(
        TextReplacementConfig
            .builder()
            .match(old)
            .replacement(new)
            .build()
    )

/**
 * Add a [Component] that will be shown when hovering above it with the mouse
 * @param hover The [Component] to show
 * @return Itself, with the hover added
 * @author Max Bossing
 * @since 0.0.1
 */
fun Component.hover(hover: Component) = hoverEvent(asHoverEvent().value(hover))

/**
 * Add a URL that will be opened when clicking the [Component]
 * @param url The URL to open
 * @return Itself, with the URL added as a [ClickEvent]
 * @author Max Bossing
 * @since 0.0.1
 */
fun Component.url(url: String) = clickEvent(ClickEvent.openUrl(url))

/**
 * Add a command that will be executed when clicking the [Component]
 * @param command The command to execute
 * @return Itself, with the command added as a [ClickEvent]
 * @author Max Bossing
 * @since 0.0.1
 */
fun Component.command(command: String) = clickEvent(ClickEvent.runCommand(command))

/**
 * Add a command that will be pasted into the chat window as a suggestion when clicking the [Component]
 * @param suggestion The suggestion to show
 * @return Itself, with the suggestion added as a [ClickEvent]
 * @author Max Bossing
 * @since 0.0.1
 */
fun Component.suggest(suggestion: String) = clickEvent(ClickEvent.suggestCommand(suggestion))

/**
 * Add a [String] that will be copied into the players clipboard when clicking the [Component]
 * @param copy The [String] to copy
 * @return Itself, with the copy added as a [ClickEvent]
 * @author Max Bossing
 * @since 0.0.1
 */
fun Component.copy(copy: String) = clickEvent(ClickEvent.copyToClipboard(copy))

/**
 * Add a callback that will be executed when clicking the [Component]
 * @param callback The Callback that will be executed when clicking the [Component]. it is the [Audience] that clicked the [Component]
 * @return Itself, with the callback added as a [ClickEvent]
 * @author Max Bossing
 * @since 0.0.1
 */
fun Component.callback(callback: (Audience) -> Unit) = clickEvent(ClickEvent.callback(callback))

/**
 * Convert a [String] into a [Component].
 * @return A [Component] containing this as Text
 * @author Max Bossing
 * @since 0.0.1
 */
fun String.component() = Component.text(this)
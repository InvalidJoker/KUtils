package de.joker.kutils.paper.commands

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import kotlin.math.max
import kotlin.math.min

/**
 * A builder for creating command exception messages with contextual information.
 * This builder constructs user-friendly error messages for commands, optionally including
 * a specified prefix, detailed error messages, and contextual information about the command input.
 *
 * @constructor Creates an instance of CommandExceptionBuilder with the specified parameters.
 * @param detailErrorMessage An optional detailed error message to include in the output.
 * @param input The command input string being processed.
 * @param cursor The position within the input where the error occurred.
 */
open class CommandExceptionBuilder(
    private val detailErrorMessage: String?,
    private val input: String?,
    private val cursor: Int
) {
    /**
     * Builds the command exception message with the given prefix.
     *
     * @return The built message
     */
    open fun build(): Component {
        val builder = text()
        val context = this.context

        if (detailErrorMessage != null) {
            builder.append(text(detailErrorMessage, NamedTextColor.YELLOW))

            builder.appendNewline()
        }

        if (context != null) {
            builder.append(text("At position $cursor: ", NamedTextColor.RED))
            builder.append(context)
        }

        return builder.build()
    }

    protected open val context: Component?
        get() {
            if (input == null || cursor < 0) {
                return null
            }

            val builder = text()
            val cursor = min(input.length, this.cursor)
            val start = max(0, (cursor - CONTEXT_AMOUNT))

            if (cursor > CONTEXT_AMOUNT) {
                builder.append(text("...", NamedTextColor.GRAY))
            }

            for (i in start..<cursor) {
                builder.append(text(input[i], NamedTextColor.RED, TextDecoration.UNDERLINED))
            }

            builder.append(Component.translatable("command.context.here", NamedTextColor.RED))

            return builder.build()
        }

    companion object {
        const val CONTEXT_AMOUNT: Int = 10
    }
}
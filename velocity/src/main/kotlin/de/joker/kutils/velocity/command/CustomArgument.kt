package de.joker.kutils.velocity.command


import com.mojang.brigadier.LiteralMessage
import com.mojang.brigadier.Message
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import com.velocitypowered.api.command.CommandSource
import dev.jorel.commandapi.CommandAPIHandler
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.CommandAPIArgumentType
import dev.jorel.commandapi.arguments.LiteralArgument
import dev.jorel.commandapi.arguments.MultiLiteralArgument
import dev.jorel.commandapi.executors.CommandArguments
import net.kyori.adventure.text.ComponentLike

/**
 * A custom argument implementation for Velocity (Kotlin version).
 */
open class CustomArgument<T, B>(
    private val base: Argument<B>,
    private val infoParser: CustomArgumentInfoParser<T, B>
) : Argument<T>(base.nodeName, base.rawType) {

    companion object {
        private const val INPUT = "%input%"
        private const val FULL_INPUT = "%finput%"
    }

    init {
        require(base !is LiteralArgument && base !is MultiLiteralArgument) {
            "${base::class.simpleName} is not a suitable base argument type for a CustomArgument"
        }
    }

    override fun getPrimitiveType(): Class<T>? = null

    override fun getArgumentType(): CommandAPIArgumentType = CommandAPIArgumentType.CUSTOM

    override fun <CommandSourceStack> parseArgument(
        cmdCtx: CommandContext<CommandSourceStack>,
        key: String,
        previousArgs: CommandArguments
    ): T {
        val rawInput = CommandAPIHandler.getRawArgumentInput(cmdCtx, key)
        val parsedInput = base.parseArgument(cmdCtx, key, previousArgs)
        val source = cmdCtx.source as? CommandSource
            ?: throw IllegalArgumentException("Command source is not a valid CommandSource")

        return try {
            infoParser.apply(CustomArgumentInfo(source, previousArgs, rawInput, parsedInput))
        } catch (e: CustomArgumentException) {
            throw e.toCommandSyntax(rawInput, cmdCtx)
        } catch (e: Exception) {
            val errorMsg = MessageBuilder("Error in executing command ")
                .appendFullInput()
                .append(" - ")
                .appendArgInput()
                .appendHere()
                .toString()
                .replace(INPUT, rawInput)
                .replace(FULL_INPUT, cmdCtx.input)
            throw SimpleCommandExceptionType { errorMsg }.create()
        }
    }

    class MessageBuilder(initial: String = "") {
        private val builder = StringBuilder(initial)

        fun appendArgInput() = apply { builder.append(INPUT) }
        fun appendFullInput() = apply { builder.append(FULL_INPUT) }
        fun appendHere() = apply { builder.append("<--[HERE]") }
        fun append(str: String) = apply { builder.append(str) }
        fun append(obj: Any?) = apply { builder.append(obj.toString()) }
        override fun toString() = builder.toString()
    }

    class CustomArgumentException private constructor() : Exception() {
        private var errorComponent: ComponentLike? = null
        private var errorMessage: String? = null
        private var errorMessageBuilder: MessageBuilder? = null

        companion object {
            fun fromAdventureComponent(component: ComponentLike) = CustomArgumentException().apply {
                errorComponent = component
            }

            fun fromString(message: String) = CustomArgumentException().apply {
                errorMessage = message
            }

            fun fromMessageBuilder(builder: MessageBuilder) = CustomArgumentException().apply {
                errorMessageBuilder = builder
            }
        }

        fun toCommandSyntax(result: String, cmdCtx: CommandContext<*>): CommandSyntaxException {
            errorComponent?.let {
                return SimpleCommandExceptionType(Message { it.toString() }).create()
            }
            errorMessageBuilder?.let {
                val msg = it.toString().replace(INPUT, result).replace(FULL_INPUT, cmdCtx.input)
                return SimpleCommandExceptionType(LiteralMessage(msg)).create()
            }
            errorMessage?.let {
                return SimpleCommandExceptionType(LiteralMessage(it)).create()
            }
            throw IllegalStateException("No error component or message specified")
        }
    }

    data class CustomArgumentInfo<B>(
        val sender: CommandSource, // Replace with your command sender abstraction
        val previousArgs: CommandArguments,
        val input: String,
        val currentInput: B
    )

    fun interface CustomArgumentInfoParser<T, B> {
        @Throws(CustomArgumentException::class)
        fun apply(info: CustomArgumentInfo<B>): T
    }
}
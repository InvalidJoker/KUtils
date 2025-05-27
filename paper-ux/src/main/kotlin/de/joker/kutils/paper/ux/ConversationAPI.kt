package de.joker.kutils.paper.ux
import de.joker.kutils.paper.PluginInstance
import dev.fruxz.stacked.extension.content
import dev.fruxz.stacked.text
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*

/**
 * A DSL-driven API for creating interactive conversations with players using Bukkit's conversation system.
 *
 * Example usage:
 * ```
 * ConversationAPI(player).conversation {
 *     question("name", text("<yellow>What's your name?")) { it.isNotBlank() }
 *     question("age", text("<yellow>How old are you?")) { it.toIntOrNull() != null }
 *
 *     done { inputs ->
 *         // Developer handles completion messaging
 *         player.sendMessage(text("<green>Welcome ${'$'}{inputs["name"]}, age ${'$'}{inputs["age"]}!"))
 *     }
 *
 *     onCancel {
 *         // Developer handles cancellation messaging
 *         player.sendMessage(text("<red>Setup cancelled."))
 *     }
 * }
 * ```
 *
 * @param player The player to whom the conversation will be presented.
 */
class ConversationAPI(private val player: Player) {

    private val questions = mutableListOf<Question>()
    private val inputs = mutableMapOf<String, String>()
    private var currentQuestionIndex = 0
    private var isActive = false
    private lateinit var onDone: (Map<String, String>) -> Unit
    private var onCancel: (() -> Unit)? = null

    companion object {
        private val activeConversations = mutableMapOf<UUID, ConversationAPI>()
        private var listenerRegistered = false

        init {
            registerListener()
        }

        private fun registerListener() {
            if (!listenerRegistered) {
                PluginInstance.server.pluginManager.registerEvents(ConversationListener, PluginInstance)
                listenerRegistered = true
            }
        }

        fun getActiveConversation(playerId: UUID): ConversationAPI? {
            return activeConversations[playerId]
        }
    }

    // Define a question class to hold the question and its validation
    data class Question(
        val identifier: String,
        val prompt: Component,
        val validator: (String) -> ValidationResult,
        val retryMessage: Component? = null
    )

    // Validation result class for better error handling
    data class ValidationResult(
        val isValid: Boolean,
        val errorMessage: Component? = null
    )

    // DSL Entry point
    fun conversation(init: ConversationBuilder.() -> Unit) {
        val builder = ConversationBuilder()
        builder.init()
        builder.build()
    }

    // DSL builder class
    inner class ConversationBuilder {
        private val questionList = mutableListOf<Question>()
        private lateinit var onComplete: (Map<String, String>) -> Unit
        private var onCancelCallback: (() -> Unit)? = null

        // Add a question to the conversation with simple boolean validator
        fun simpleQuestion(identifier: String, prompt: Component, validator: (String) -> Boolean) {
            questionList.add(Question(
                identifier,
                prompt,
                { input -> ValidationResult(validator(input)) }
            ))
        }

        // Add a question with advanced validation and custom error message
        fun question(
            identifier: String,
            prompt: Component,
            validator: (String) -> ValidationResult
        ) {
            questionList.add(Question(identifier, prompt, validator))
        }

        // Add a question with custom retry message
        fun questionWithRetry(
            identifier: String,
            prompt: Component,
            retryMessage: Component,
            validator: (String) -> Boolean
        ) {
            questionList.add(Question(
                identifier,
                prompt,
                { input -> ValidationResult(validator(input)) },
                retryMessage
            ))
        }

        fun validatedQuestion(
            identifier: String,
            prompt: Component,
            validator: ConversationValidators.() -> (String) -> ValidationResult
        ) {
            questionList.add(Question(identifier, prompt, ConversationValidators.validator()))
        }

        // Add a question with custom retry message and advanced validation
        fun validatedQuestionWithRetry(
            identifier: String,
            prompt: Component,
            retryMessage: Component,
            validator: ConversationValidators.() -> (String) -> ValidationResult
        ) {
            questionList.add(Question(
                identifier,
                prompt,
                ConversationValidators.validator(),
                retryMessage
            ))
        }

        // Define the done function to finalize the conversation
        fun done(onComplete: (Map<String, String>) -> Unit) {
            this.onComplete = onComplete
        }

        // Define what happens when conversation is cancelled
        fun onCancel(callback: () -> Unit) {
            this.onCancelCallback = callback
        }

        // Build and start the conversation
        fun build() {
            questions.addAll(questionList)
            onDone = onComplete
            onCancel = onCancelCallback
            startConversation()
        }
    }

    private fun startConversation() {
        if (questions.isEmpty()) return

        isActive = true
        currentQuestionIndex = 0
        inputs.clear()
        activeConversations[player.uniqueId] = this

        askCurrentQuestion()
    }

    private fun askCurrentQuestion() {
        if (currentQuestionIndex < questions.size) {
            val question = questions[currentQuestionIndex]
            player.sendMessage(question.prompt)
        }
    }

    internal fun handleInput(input: String) {
        if (!isActive) return

        // Handle cancel command
        if (input.equals("cancel", ignoreCase = true)) {
            cancelConversation()
            return
        }

        val question = questions[currentQuestionIndex]
        val validationResult = question.validator(input)

        if (validationResult.isValid) {
            // Store the valid input
            inputs[question.identifier] = input
            currentQuestionIndex++

            // Check if we have more questions
            if (currentQuestionIndex < questions.size) {
                askCurrentQuestion()
            } else {
                // Conversation complete
                completeConversation()
            }
        } else {
            // Invalid input, show error and retry
            val errorMessage = validationResult.errorMessage
                ?: question.retryMessage
                ?: text("<red>Invalid input. Please try again.")

            player.sendMessage(errorMessage)
            // Ask the same question again
            askCurrentQuestion()
        }
    }

    private fun completeConversation() {
        isActive = false
        activeConversations.remove(player.uniqueId)

        // Call the completion callback
        onDone(inputs.toMap())
    }

    private fun cancelConversation() {
        isActive = false
        activeConversations.remove(player.uniqueId)

        // Call the cancel callback if defined
        onCancel?.invoke()
    }

    // Force end conversation (useful for cleanup)
    fun endConversation() {
        if (isActive) {
            cancelConversation()
        }
    }

    // Check if conversation is active
    fun isConversationActive(): Boolean = isActive

    // Get current progress
    fun getProgress(): Pair<Int, Int> = Pair(currentQuestionIndex + 1, questions.size)

    object ConversationListener : Listener {

        @EventHandler(priority = EventPriority.LOWEST)
        fun onPlayerChat(event: AsyncChatEvent) {
            val conversation = getActiveConversation(event.player.uniqueId)
            if (conversation != null && conversation.isConversationActive()) {
                event.isCancelled = true

                val msg = event.message().content

                // Handle input on main thread
                PluginInstance.server.scheduler.runTask(PluginInstance, Runnable {
                    conversation.handleInput(msg)
                })
            }
        }

        @EventHandler
        fun onPlayerQuit(event: PlayerQuitEvent) {
            val conversation = getActiveConversation(event.player.uniqueId)
            conversation?.endConversation()
        }
    }
}

// Extension functions for common validators
object ConversationValidators {

    fun notEmpty(): (String) -> ConversationAPI.ValidationResult = { input ->
        ConversationAPI.ValidationResult(
            input.isNotBlank(),
            text("<red>Input cannot be empty!")
        )
    }

    fun minLength(min: Int): (String) -> ConversationAPI.ValidationResult = { input ->
        ConversationAPI.ValidationResult(
            input.length >= min,
            text("<red>Input must be at least $min characters long!")
        )
    }

    fun maxLength(max: Int): (String) -> ConversationAPI.ValidationResult = { input ->
        ConversationAPI.ValidationResult(
            input.length <= max,
            text("<red>Input must be at most $max characters long!")
        )
    }

    fun isNumber(): (String) -> ConversationAPI.ValidationResult = { input ->
        ConversationAPI.ValidationResult(
            input.toIntOrNull() != null,
            text("<red>Please enter a valid number!")
        )
    }

    fun isInRange(min: Int, max: Int): (String) -> ConversationAPI.ValidationResult = { input ->
        val number = input.toIntOrNull()
        ConversationAPI.ValidationResult(
            number != null && number in min..max,
            text("<red>Please enter a number between $min and $max!")
        )
    }

    fun matches(regex: Regex, errorMessage: String = "Invalid format!"): (String) -> ConversationAPI.ValidationResult = { input ->
        ConversationAPI.ValidationResult(
            regex.matches(input),
            text("<red>$errorMessage")
        )
    }

    fun oneOf(vararg options: String, ignoreCase: Boolean = true): (String) -> ConversationAPI.ValidationResult = { input ->
        val isValid = if (ignoreCase) {
            options.any { it.equals(input, ignoreCase = true) }
        } else {
            input in options
        }
        ConversationAPI.ValidationResult(
            isValid,
            text("<red>Please choose one of: ${options.joinToString(", ")}")
        )
    }
}
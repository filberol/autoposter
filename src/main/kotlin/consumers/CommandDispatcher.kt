package ru.social.ai.consumers

import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import org.telegram.telegrambots.meta.api.objects.Update
import ru.social.ai.commands.Command
import ru.social.ai.commands.NotFound
import ru.social.ai.commands.ReplyDirectlyWithAi
import ru.social.ai.commands.Test

private const val CommandExecutionTimeoutMs = 30000L

class CommandDispatcher {

    companion object {
        private val registeredCommands = listOf(
            Test(),
            ReplyDirectlyWithAi()
        )
    }

    suspend fun dispatchCommand(update: Update) {
        val foundCommand: Command? = registeredCommands.firstOrNull { update.message.text.startsWith(it.name) }
        withTimeout(CommandExecutionTimeoutMs) {
            launch {
                foundCommand?.let { foundCommand.execute(update) }
                if (foundCommand == null) {
                    NotFound().execute(update)
                }
            }
        }
    }
}

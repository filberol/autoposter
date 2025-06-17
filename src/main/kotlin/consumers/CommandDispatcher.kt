package ru.social.ai.consumers

import org.telegram.telegrambots.meta.api.objects.Update
import ru.social.ai.commands.Command
import ru.social.ai.commands.NotFoundCommand
import ru.social.ai.commands.TestCommand

class CommandDispatcher {
    companion object {
        private val registeredCommands = listOf(
            TestCommand()
        )
    }

    fun dispatchCommand(update: Update) {
        val foundCommand: Command? = registeredCommands.firstOrNull { update.message.text.startsWith(it.name) }
        if (foundCommand == null) {
            NotFoundCommand().execute(update)
        } else {
            foundCommand.execute(update)
        }
    }
}

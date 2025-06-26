package ru.social.ai.commands.debug

import org.telegram.telegrambots.meta.api.objects.Update
import ru.social.ai.commands.base.AdminCommand

class DebugUpdate(
    override val triggerName: String,
): AdminCommand() {
    override suspend fun execute(update: Update) {
        println(update)
    }
}

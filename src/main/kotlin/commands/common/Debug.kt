package ru.social.ai.commands.common

import org.telegram.telegrambots.meta.api.objects.Update
import ru.social.ai.commands.base.AdminCommand

class Debug(
    override val triggerName: String,
): AdminCommand() {
    override suspend fun execute(update: Update) {
        println(update)
    }
}

package ru.social.ai.commands.common

import org.telegram.telegrambots.meta.api.objects.Update
import ru.social.ai.commands.base.Basic

class Debug(
    override val triggerName: String,
): Basic() {
    override suspend fun execute(update: Update) {
        println(update)
    }
}

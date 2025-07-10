package ru.social.ai.commands.base.multistage

import org.telegram.telegrambots.meta.api.objects.Update
import ru.social.ai.commands.base.Basic
import ru.social.ai.commands.base.WithContext
import ru.social.ai.commands.base.context.CommandContext
import ru.social.ai.prebuilders.SendMessagePreBuilder

abstract class Stage : Basic(), WithContext {
    override val triggerName = "command_not_found"
    abstract val successPhrase: String
    override lateinit var context: CommandContext

    fun sendCommandPhrase(update: Update) {
        telegramClient.execute(
            SendMessagePreBuilder.chatId(update.message.chatId.toString()).text(successPhrase).build()
        )
    }
}

package ru.social.ai.commands.base

import org.telegram.telegrambots.meta.api.objects.Update
import ru.social.ai.prebuilders.SendMessagePreBuilder

abstract class Stage : Basic() {
    override val triggerName = "command_not_found"
    abstract val successPhrase: String
    lateinit var context: CommandContext

    fun sendCommandPhrase(update: Update) {
        telegramClient.execute(
            SendMessagePreBuilder.chatId(update.message.chatId.toString()).text(successPhrase).build()
        )
    }
}

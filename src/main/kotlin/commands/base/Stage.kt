package ru.social.ai.commands.base

import org.telegram.telegrambots.meta.api.objects.Update
import ru.social.ai.prebuilders.SendMessagePreBuilder

abstract class Stage : Basic() {
    abstract val commandPhrase: String

    fun sendCommandPhrase(update: Update) {
        telegramClient.execute(
            SendMessagePreBuilder.chatId(update.message.chatId.toString()).text(commandPhrase).build()
        )
    }
}

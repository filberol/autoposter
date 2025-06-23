package ru.social.ai.commands.common

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import ru.social.ai.commands.base.Basic
import ru.social.ai.util.TextExtractor

class Test(
    override val triggerName: String
) : Basic() {
    override suspend fun execute(update: Update) {
        telegramClient.execute(
            SendMessage(
                update.message.chatId.toString(),
                TextExtractor.extractText(update)
            )
        )
    }
}

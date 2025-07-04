package ru.social.ai.commands.common

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import ru.social.ai.commands.base.AdminCommand
import ru.social.ai.util.extractText

class Test(
    override val triggerName: String
) : AdminCommand() {
    override suspend fun execute(update: Update) {
        telegramClient.execute(
            SendMessage(
                update.message.chatId.toString(),
                update.extractText()
            )
        )
    }
}

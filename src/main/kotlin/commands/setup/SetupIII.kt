package ru.social.ai.commands.setup

import org.telegram.telegrambots.meta.api.objects.Update
import ru.social.ai.commands.base.Stage
import ru.social.ai.prebuilders.SendMessagePreBuilder

class SetupIII: Stage() {
    override val successPhrase = "Успех!"

    override suspend fun execute(update: Update) {
        SendMessagePreBuilder.chatId(update.message.chatId).text("Этап завершен").build().also { telegramClient.execute(it) }
    }
}

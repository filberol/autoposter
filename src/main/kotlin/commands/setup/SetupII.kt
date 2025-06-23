package ru.social.ai.commands.setup

import org.telegram.telegrambots.meta.api.objects.Update
import ru.social.ai.commands.base.Stage
import ru.social.ai.prebuilders.SendMessagePreBuilder

class SetupII: Stage() {
    override val commandPhrase = "Теперь второй этап, он завершится автоматически"

    override suspend fun execute(update: Update) {
        SendMessagePreBuilder.chatId(update.message.chatId).text("Этап завершен").build().also { telegramClient.execute(it) }
    }
}

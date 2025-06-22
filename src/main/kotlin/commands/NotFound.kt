package ru.social.ai.commands

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import ru.social.ai.commands.base.Basic

class NotFound: Basic() {
    override suspend fun execute(update: Update) {
        val sendMessage = SendMessage(update.message.chatId.toString(), "Команда не найдена")
        try {
            telegramClient.execute(sendMessage)
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }
}

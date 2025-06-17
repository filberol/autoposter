package ru.social.ai.commands

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException


class Test: Basic() {
    override val name = "/test"
    override suspend fun execute(update: Update) {
        if (update.hasMessage() && update.message.hasText()) {
            val sendMessage = SendMessage(update.message.chatId.toString(), update.message.text)
            try {
                telegramClient.execute(sendMessage)
            } catch (e: TelegramApiException) {
                e.printStackTrace()
            }
        }
    }
}

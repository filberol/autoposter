package ru.social.ai.commands

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

class NotFoundCommand: BasicCommand() {
    override val name = "" // Override to never register such
    override fun execute(update: Update) {
        val sendMessage = SendMessage(update.message.chatId.toString(), "Команда не найдена")
        try {
            client.execute(sendMessage)
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }
}

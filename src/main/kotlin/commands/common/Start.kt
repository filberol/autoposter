package ru.social.ai.commands.common

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import ru.social.ai.commands.base.Basic

class Start(
    override val triggerName: String
): Basic() {

    override suspend fun execute(update: Update) {
        telegramClient.execute(SendMessage.builder()
            .chatId(update.message.chatId)
            .text("""
                Привет!
                Я бот АвтоПостер, могу вести за тебя каналы и создавать контент. Для начала работы добавь меня
                 @gptautoposter_bot в свой канал (да-да, мы не забираем владение каналом) и не забудь сделать
                 его администратором. После присылай /new и приступай к настройке постинга 😊
            """.trimIndent())
            .build()
        )
    }
}

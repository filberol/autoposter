package ru.social.ai.commands

import io.github.sashirestela.openai.domain.chat.ChatMessage.UserMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import ru.social.ai.prebuilders.FreeModelPreBuilder
import ru.social.ai.prebuilders.SendMessagePreBuilder
import java.util.concurrent.ExecutionException

class ReplyDirectlyWithAi: Basic() {
    override val name = "/reply"

    override suspend fun execute(update: Update) {
        if (update.hasMessage() && update.message.hasText()) {
            try {
                val response = chatClient.chatCompletions().create(
                    FreeModelPreBuilder
                        .messages(listOf(UserMessage.of(update.message.text)) )
                        .build()
                )
                val sendMessage = SendMessagePreBuilder
                    .chatId(update.message.chatId)
                    .text(withContext(Dispatchers.IO) {
                        response.get()
                    }.choices.first().message.content.also { println(it) })
                    .build()
                telegramClient.execute(sendMessage)
            } catch (e: TelegramApiException) {
                e.printStackTrace()
            } catch (e: ExecutionException) {
                e.printStackTrace()
                telegramClient.execute(SendMessage(update.message.chatId.toString(), "Сервис временно недоступен"))
            }
        }
    }
}

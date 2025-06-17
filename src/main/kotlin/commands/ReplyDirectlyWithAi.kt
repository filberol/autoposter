package ru.social.ai.commands

import io.github.sashirestela.openai.domain.chat.ChatMessage.UserMessage
import io.github.sashirestela.openai.domain.chat.ChatRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.util.concurrent.ExecutionException

class ReplyDirectlyWithAi: Basic() {
    override val name = "/reply"

    override suspend fun execute(update: Update) {
        if (update.hasMessage() && update.message.hasText()) {
            try {
                val response = chatClient.chatCompletions().create(
                    ChatRequest.builder()
                        .model("deepseek/deepseek-chat-v3-0324:free")
                        .messages(listOf(UserMessage.of(update.message.text)) )
                        .build()
                )
                val sendMessage = SendMessage.builder()
                    .chatId(update.message.chatId)
                    .text(withContext(Dispatchers.IO) {
                        response.get()
                    }.choices.first().message.content.also { println(it) })
                    .parseMode(ParseMode.MARKDOWN)
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

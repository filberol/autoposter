package ru.social.ai.commands.debug

import io.github.sashirestela.openai.domain.chat.ChatMessage.UserMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.telegram.telegrambots.meta.api.objects.Update
import ru.social.ai.commands.base.AdminCommand
import ru.social.ai.prebuilders.FreeModelPreBuilder
import ru.social.ai.prebuilders.SendMessagePreBuilder
import ru.social.ai.util.TextExtractor

class ReplyDirectlyWithAi(
    override val triggerName: String,
): AdminCommand() {
    override suspend fun execute(update: Update) {
        val text = TextExtractor.extractTextWithoutCommand(update)
        val response = chatClient.chatCompletions().create(
            FreeModelPreBuilder
                .messages(listOf(UserMessage.of(text)) )
                .build()
        )
        val sendMessage = SendMessagePreBuilder
            .chatId(update.message.chatId)
            .text(withContext(Dispatchers.IO) {
                response.get().choices.first().message.content
            })
            .build()
        telegramClient.execute(sendMessage)
    }
}

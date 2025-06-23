package ru.social.ai.ai.prompts

import io.github.sashirestela.openai.domain.chat.ChatMessage.SystemMessage
import io.github.sashirestela.openai.domain.chat.ChatMessage.UserMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.social.ai.clients.ChatBot
import ru.social.ai.prebuilders.FreeModelPreBuilder
import ru.social.ai.util.MessageFormatter

object SimpleDialog {
    private val chatClient = ChatBot.getRelevantClient()

    suspend fun getRephrase(question: String): String {
        val response = chatClient.chatCompletions().create(
            FreeModelPreBuilder
                .messages(listOf(
                    SystemMessage.of(BasicRephrasePrompt),
                    UserMessage.of(question),
                ) )
                .build()
        )
        return MessageFormatter.formatForTelegramMarkup(withContext(Dispatchers.IO) {
            response.get().choices.first().message.content
        })
    }
}

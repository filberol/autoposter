package ru.social.ai.ai

import io.github.sashirestela.openai.domain.chat.ChatMessage.SystemMessage
import io.github.sashirestela.openai.domain.chat.ChatMessage.UserMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.social.ai.ai.prompts.BasicRephrasePrompt
import ru.social.ai.ai.prompts.CompileSourcesPrompt
import ru.social.ai.ai.prompts.parseLine
import ru.social.ai.clients.ChatBot
import ru.social.ai.prebuilders.FreeModelPreBuilder
import ru.social.ai.util.formatForTelegramMarkup

object SimpleDialog {
    private val chatClient = ChatBot.client

    suspend fun getRephrase(question: String): String {
        val response = chatClient.chatCompletions().create(
            FreeModelPreBuilder
                .messages(listOf(
                    SystemMessage.of(BasicRephrasePrompt),
                    UserMessage.of(question),
                ) )
                .build()
        )
        return withContext(Dispatchers.IO) {
            response.get().choices.first().message.content.formatForTelegramMarkup()
        }
    }

    suspend fun getPostCompilationWithDecision(promptBody: String): Pair<Int, String> {
        val response = chatClient.chatCompletions().create(
            FreeModelPreBuilder
                .messages(listOf(
                    SystemMessage.of(CompileSourcesPrompt),
                    UserMessage.of(promptBody),
                ) )
                .build()
        )
        val text = withContext(Dispatchers.IO) {
            response.get().choices.first().message.content.formatForTelegramMarkup()
        }
        return parseLine(text)
    }
}

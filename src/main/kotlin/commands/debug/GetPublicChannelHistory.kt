package ru.social.ai.commands.debug

import org.telegram.telegrambots.meta.api.objects.Update
import ru.social.ai.commands.base.AdminCommand
import ru.social.ai.prebuilders.SendMessagePreBuilder
import ru.social.ai.util.extractText
import ru.social.ai.util.extractTextWithoutCommand
import ru.social.ai.util.getMessageHistoryPublicChannel

class GetPublicChannelHistory(
    override val triggerName: String
) : AdminCommand() {
    override suspend fun execute(update: Update) {
        val channelLink = update.extractTextWithoutCommand().trim()
        val messages = getMessageHistoryPublicChannel(channelLink, 5)
            .map { it.content.extractText() }

        telegramClient.execute(
            SendMessagePreBuilder.chatId(update.message.chatId)
            .text(messages.joinToString("\n-------------\n")).build()
        )
    }
}

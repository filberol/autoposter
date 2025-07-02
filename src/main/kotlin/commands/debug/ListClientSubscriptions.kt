package ru.social.ai.commands.debug

import org.telegram.telegrambots.meta.api.objects.Update
import ru.social.ai.commands.base.AdminCommand
import ru.social.ai.prebuilders.SendMessagePreBuilder
import ru.social.ai.util.getPublicChannelFromLink
import ru.social.ai.util.getSubscribedChannels

class ListClientSubscriptions(
    override val triggerName: String
) : AdminCommand() {
    override suspend fun execute(update: Update) {
        val channels = getSubscribedChannels()
            .map { getPublicChannelFromLink(it) }
        val response =
            "Список подписок клиента:\n" +
                    channels.joinToString(separator = "\n") {
                        "${it.title} - ${it.unreadCount} непрочитанных"
                    }
        telegramClient.execute(
            SendMessagePreBuilder
                .chatId(update.message.chatId.toString())
                .text(response)
                .build()
        )
    }
}

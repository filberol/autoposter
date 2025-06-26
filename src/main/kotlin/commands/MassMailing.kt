package ru.social.ai.commands

import org.jetbrains.exposed.sql.selectAll
import org.telegram.telegrambots.meta.api.objects.Update
import ru.social.ai.commands.base.AdminCommand
import ru.social.ai.db.entities.ChannelConfigurations
import ru.social.ai.db.entities.toChannelConfiguration
import ru.social.ai.prebuilders.SendMessagePreBuilder
import ru.social.ai.util.TextExtractor.extractTextWithoutCommand

class MassMailing(
    override val triggerName: String
) : AdminCommand() {
    override suspend fun execute(update: Update) {
        val channels = ChannelConfigurations.selectAll()
            .map { it.toChannelConfiguration().linkId }
            .toSet()
        val text = extractTextWithoutCommand(update)

        channels.forEach { channel ->
            SendMessagePreBuilder.chatId(channel).text(text).build()
                .also { telegramClient.execute(it) }
        }
    }
}

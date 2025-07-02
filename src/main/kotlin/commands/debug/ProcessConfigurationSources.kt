package ru.social.ai.commands.debug

import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.telegram.telegrambots.meta.api.objects.Update
import ru.social.ai.commands.base.AdminCommand
import ru.social.ai.db.entities.ChannelConfigurations
import ru.social.ai.db.entities.ChannelConfigurations.linkId
import ru.social.ai.db.entities.ChannelConfigurations.owner
import ru.social.ai.db.entities.toChannelConfiguration
import ru.social.ai.prebuilders.SendMessagePreBuilder
import ru.social.ai.util.extractText
import ru.social.ai.util.extractTextWithoutCommand
import ru.social.ai.util.getAdministratedChannelFromLink
import ru.social.ai.util.getLastMessageFromChannelLink

class ProcessConfigurationSources(
    override val triggerName: String
) : AdminCommand() {
    override suspend fun execute(update: Update) {
        val channelInfo = getAdministratedChannelFromLink(
            update.extractTextWithoutCommand().trim()
        )
        val config = ChannelConfigurations.select {
            (owner eq update.message.from.id) and
                    (linkId eq channelInfo.id)
        }.map { it.toChannelConfiguration() }.first()
        val messages = config.informationSources
            .map { getLastMessageFromChannelLink(it).content.extractText() }

        telegramClient.execute(SendMessagePreBuilder.chatId(update.message.chatId)
            .text(messages.joinToString("\n-------------\n")).build())
    }
}

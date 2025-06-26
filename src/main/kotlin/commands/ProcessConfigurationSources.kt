package ru.social.ai.commands

import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.telegram.telegrambots.meta.api.objects.Update
import ru.social.ai.commands.base.Basic
import ru.social.ai.db.entities.ChannelConfigurations
import ru.social.ai.db.entities.ChannelConfigurations.linkId
import ru.social.ai.db.entities.ChannelConfigurations.owner
import ru.social.ai.db.entities.toChannelConfiguration
import ru.social.ai.util.TelegramUtils.getChannelFromLink
import ru.social.ai.util.TextExtractor.extractTextWithoutCommand

class ProcessConfigurationSources(
    override val triggerName: String
) : Basic() {
    override suspend fun execute(update: Update) {
        val link = extractTextWithoutCommand(update).trim()
        val channelInfo = getChannelFromLink(link)
        val config = ChannelConfigurations.select {
            (owner eq update.message.from.id) and
                    (linkId eq channelInfo.id)
        }.map { it.toChannelConfiguration() }.first()

    }
}

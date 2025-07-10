package ru.social.ai.commands.setup

import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.update
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import ru.social.ai.commands.base.multistage.Stage
import ru.social.ai.db.entities.ChannelConfigurations
import ru.social.ai.db.entities.ChannelConfigurations.linkId
import ru.social.ai.db.entities.ChannelConfigurations.owner
import ru.social.ai.util.extractText
import ru.social.ai.util.getAdministratedChannelFromLink

object SetupIV : Stage() {
    override val successPhrase = "Конфигурация канала успешно создана!"

    override suspend fun execute(update: Update) {
        try {
            val channelInfos = update.extractText()
                .split("\n")
                .map { getAdministratedChannelFromLink(it) }
            val channelId = context.get<Long>("channel_id")
            ChannelConfigurations.update({
                (owner eq update.message.from.id) and
                        (linkId eq channelId!!)
            }) {
                it[informationSources] = channelInfos.map { ch -> ch.id }.joinToString(",")
            }
        } catch (e: TelegramApiException) {
            println(e.message)
            throw e
        }
    }
}

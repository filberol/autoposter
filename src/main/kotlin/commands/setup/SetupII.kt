package ru.social.ai.commands.setup

import org.jetbrains.exposed.sql.replace
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import ru.social.ai.commands.base.Stage
import ru.social.ai.db.entities.ChannelConfigurations
import ru.social.ai.exceptions.UserReasonableException
import ru.social.ai.util.TelegramUtils.getChatFromLink
import ru.social.ai.util.TextExtractor.extractText

class SetupII: Stage() {
    override val successPhrase = "Теперь второй этап, он завершится автоматически"

    override suspend fun execute(update: Update) {
        val text = extractText(update)
        try {
            val channel = getChatFromLink(text)
            val newConf = ChannelConfigurations.replace {
                it[linkId] = channel.id
                it[name] = channel.title
                it[owner] = update.message.from.id
            }
            context.set("config_id", newConf[ChannelConfigurations.id])
        } catch (e: TelegramApiException) {
            if (e.message?.contains("chat not found", true) == true) {
                throw UserReasonableException("Канал не найден. Бот должен быть назначен администратором канала.")
            } else {
                throw e
            }
        }
    }
}

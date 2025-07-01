package ru.social.ai.commands.setup

import org.jetbrains.exposed.sql.replace
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import ru.social.ai.commands.base.Stage
import ru.social.ai.db.entities.ChannelConfigurations
import ru.social.ai.exceptions.UserReasonableException
import ru.social.ai.util.TextExtractor.extractText
import ru.social.ai.util.getAdministratedChannelFromLink

object SetupII : Stage() {
    override val successPhrase = """
        Теперь определимся с типажом. 
        Опишите вкратце, как должен вести себя редактор, какие фразы использовать, стиль (формальный/деловой), 
        относиться ли скептически к новостям или оптимистично. Добавьте необходимые нюансы. 
        От ваших инструкций будет зависеть качество контента.
        """.trimMargin().replace("\n", "")

    override suspend fun execute(update: Update) {
        val text = extractText(update)
        try {
            val channel = getAdministratedChannelFromLink(text)
            ChannelConfigurations.replace {
                it[linkId] = channel.id
                it[name] = channel.title
                it[owner] = update.message.from.id
            }
            context.set("channel_id", channel.id)
        } catch (e: TelegramApiException) {
            if (e.message?.contains("chat not found", true) == true) {
                throw UserReasonableException("Канал не найден. Бот должен быть назначен администратором канала.")
            } else {
                throw e
            }
        }
    }
}

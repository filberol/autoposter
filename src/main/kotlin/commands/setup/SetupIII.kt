package ru.social.ai.commands.setup

import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.update
import org.telegram.telegrambots.meta.api.objects.Update
import ru.social.ai.commands.base.multistage.Stage
import ru.social.ai.db.entities.ChannelConfigurations
import ru.social.ai.db.entities.ChannelConfigurations.linkId
import ru.social.ai.db.entities.ChannelConfigurations.owner
import ru.social.ai.util.extractText

object SetupIII : Stage() {
    override val successPhrase = """
        Последний этап! 
        Пришли от трех до пяти каналов, на которых будет основываться бот. 
        Это важно, потому что именно эта настройка задает тренды и фактическое содержание.
        """.trimIndent().replace("\n", "")

    override suspend fun execute(update: Update) {
        val text = update.extractText()
        val channelId = context.get<Long>("channel_id")
        ChannelConfigurations.update({
            (owner eq update.message.from.id) and
                    (linkId eq channelId!!)
        }) {
            it[rephrasePrompt] = text
        }
    }
}

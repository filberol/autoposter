package ru.social.ai.commands.setup

import org.telegram.telegrambots.meta.api.objects.Update
import ru.social.ai.commands.base.Stage
import ru.social.ai.db.entities.ChannelConfigurationEntity
import ru.social.ai.util.TextExtractor.extractText

class SetupIII: Stage() {
    override val successPhrase = """
        |Теперь определимся с типажом. 
        |Опишите вкратце, как должен вести себя редактор, какие фразы использовать, стиль (формальный/деловой), 
        |относиться ли скептически к новостям или оптимистично. Добавьте необходимые нюансы. 
        |От ваших инструкций будет зависеть качество контента.
        """".trimMargin().replace("\n", "")

    override suspend fun execute(update: Update) {
        val text = extractText(update)
        val config = context.get<Int>("config_id")?.let { ChannelConfigurationEntity.findById(it) }!!
        config!!.

    }
}

package ru.social.ai.commands.common

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.telegram.telegrambots.meta.api.objects.Update
import ru.social.ai.commands.base.Basic
import ru.social.ai.db.entities.UserCommandStages

class Cancel(
    override val triggerName: String
) : Basic() {
    override suspend fun execute(update: Update) {
        val user = update.message.from.id
        UserCommandStages.deleteWhere { id eq user }
    }
}

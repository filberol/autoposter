package ru.social.ai.commands.base

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.replace
import org.telegram.telegrambots.meta.api.objects.Update
import ru.social.ai.db.entities.UserCommandStage
import ru.social.ai.db.entities.UserCommandStageEntity
import ru.social.ai.db.entities.UserCommandStages
import ru.social.ai.db.entities.UserCommandStages.commandName
import ru.social.ai.db.entities.UserCommandStages.id
import ru.social.ai.util.getUserId

abstract class MultiStage(
    override val triggerName: String
) : Basic() {
    abstract val stages: List<Stage>

    override suspend fun execute(update: Update) {
        val userId = update.getUserId()
        val context = CommandContextProvider.getOrCreate(userId)

        val retrievedStage =
            UserCommandStageEntity
                .find { (id eq userId) and (commandName eq triggerName) }
                .firstOrNull()?.toCommandStage() ?: UserCommandStage(userId, triggerName, 0)
        val currentStage = stages[retrievedStage.commandStage].also {
            it.context = context
        }
        currentStage.apply {
            execute(update)
            sendCommandPhrase(update)
        }
        if (retrievedStage.commandStage + 1 == stages.size) { // Last one
            CommandContextProvider.clear(userId)
            UserCommandStages.deleteWhere { (id eq userId) and (commandName eq triggerName) }
        } else {
            UserCommandStages.replace {
                it[id] = retrievedStage.userId
                it[commandName] = retrievedStage.commandName
                it[commandStage] = retrievedStage.commandStage + 1
            }
        }
    }
}

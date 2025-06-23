package ru.social.ai.entities

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable

object UserCommandStages : IdTable<Long>() {
    override val id = long("user_id").entityId()
    val commandName = varchar("command_name", 32)
    val commandStage = integer("command_stage")
}

class UserCommandStageEntity(userId: EntityID<Long>) : LongEntity(userId) {
    companion object : LongEntityClass<UserCommandStageEntity>(UserCommandStages)

    private var commandName by UserCommandStages.commandName
    private var commandStage by UserCommandStages.commandStage

    fun toCommandStage() = UserCommandStage(
        id.value, commandName, commandStage
    )
}

data class UserCommandStage(
    val userId: Long,
    val commandName: String,
    val commandStage: Int
)

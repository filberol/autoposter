package ru.social.ai.db.entities

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import ru.social.ai.db.Schema

@Schema
object UserCommandStages : IdTable<Long>() {
    override val id = long("user_id").entityId()
    val commandName = varchar("command_name", 32)
    val commandStage = integer("command_stage")

    override val primaryKey = PrimaryKey(id)
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

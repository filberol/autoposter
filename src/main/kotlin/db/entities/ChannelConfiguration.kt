package ru.social.ai.db.entities

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import ru.social.ai.db.Schema

@Schema
object ChannelConfigurations : IdTable<Int>() {
    override val id = integer("id").autoIncrement().entityId()
    val linkId = long("channel_link_id")
    val name = varchar("channel_name", 128)
    val owner = long("owner_id")
    val rephrasePrompt = text("rephrase_prompt").nullable()
    val informationSources = text("information_sources").nullable()

    override val primaryKey = PrimaryKey(id)
}

class ChannelConfigurationEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ChannelConfigurationEntity>(ChannelConfigurations)

    private var name by ChannelConfigurations.name
    private var linkId by ChannelConfigurations.linkId
    private var owner by ChannelConfigurations.owner
    private var rephrasePrompt by ChannelConfigurations.rephrasePrompt
    private var informationSources by ChannelConfigurations.informationSources

    fun toChannelConfiguration() = ChannelConfiguration(
        id.value, name, linkId, owner, rephrasePrompt, informationSources?.split(',')
    )
}

data class ChannelConfiguration(
    val id: Int,
    val name: String,
    val linkId: Long,
    val owner: Long,
    val rephrasePrompt: String?,
    val informationSources: List<String>?,
)

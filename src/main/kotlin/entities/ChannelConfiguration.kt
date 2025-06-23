package ru.social.ai.entities

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable

object ChannelConfigurations : IdTable<Int>() {
    override val id = integer("id").entityId().autoIncrement()
    val name = varchar("channel_name", 128)
    val link = varchar("channel_link", 128)
    val owner = long("owner_id")
    val rephrasePrompt = text("rephrase_prompt")
    val informationSources = text("information_sources")
}

class ChannelConfigurationEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ChannelConfigurationEntity>(ChannelConfigurations)

    private var name by ChannelConfigurations.name
    private var link by ChannelConfigurations.link
    private var owner by ChannelConfigurations.owner
    private var rephrasePrompt by ChannelConfigurations.rephrasePrompt
    private var informationSources by ChannelConfigurations.informationSources

    fun toChannelConfiguration() = ChannelConfiguration(
        id.value, name, link, owner, rephrasePrompt, informationSources.split(',')
    )
}

data class ChannelConfiguration(
    val id: Int,
    val name: String,
    val link: String,
    val owner: Long,
    val rephrasePrompt: String,
    val informationSources: List<String>,
)

package ru.social.ai.db.entities

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import ru.social.ai.db.Schema

@Schema
object ChannelConfigurations : Table() {
    val linkId = long("channel_link_id")
    val name = varchar("channel_name", 128)
    val owner = long("owner_id")
    val rephrasePrompt = text("rephrase_prompt").nullable()
    val informationSources = text("information_sources").nullable()

    override val primaryKey = PrimaryKey(owner, linkId)
}

fun ResultRow.toChannelConfiguration() = ChannelConfiguration(
    linkId = this[ChannelConfigurations.linkId],
    name = this[ChannelConfigurations.name],
    owner = this[ChannelConfigurations.owner],
    rephrasePrompt = this[ChannelConfigurations.rephrasePrompt],
    informationSources = this[ChannelConfigurations.informationSources]
        ?.split(",")?.map { it.toLong() } ?: emptyList()
)

data class ChannelConfiguration(
    val name: String,
    val linkId: Long,
    val owner: Long,
    val rephrasePrompt: String?,
    val informationSources: List<Long>,
)

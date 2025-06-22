package ru.social.ai.entities

data class ChannelConfiguration(
    val name: String,
    val link: String,
    val owner: Long,
    val rephrasePrompt: String,
    val informationSources: List<String>,
)
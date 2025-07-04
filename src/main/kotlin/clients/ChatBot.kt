package ru.social.ai.clients

import io.github.sashirestela.openai.SimpleOpenAI

object ChatBot {
    val client: SimpleOpenAI by lazy { getRelevantClient() }

    private val freeToken = System.getenv("openrouter_api_token_free")
    private val deepSeekToken = System.getenv("deepseek_api_token")
    private val gigaChatToken = System.getenv("giga_chat_token") // При необходимости ру пополнения

    private val freeClient = SimpleOpenAI.builder()
        .apiKey(freeToken)
        .baseUrl("https://openrouter.ai/api")
        .build()

    private val deepSeek = SimpleOpenAI.builder() // Для MVP платный клиент не используется
        .apiKey(deepSeekToken)
        .baseUrl("https://api.deepseek.com")
        .build()

    private fun getRelevantClient(): SimpleOpenAI = freeClient
}

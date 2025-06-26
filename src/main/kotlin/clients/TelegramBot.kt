package ru.social.ai.clients

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient
import ru.social.ai.util.TokenProvider

object TelegramBot {
    val client: OkHttpTelegramClient by lazy { getHttpClient() }

    private val token = TokenProvider.getTelegramToken()

    private fun getHttpClient() = OkHttpTelegramClient(token)
}

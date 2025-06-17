package ru.social.ai.clents

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient
import org.telegram.telegrambots.meta.generics.TelegramClient
import ru.social.ai.util.TokenProvider

class TelegramBot {
    companion object {
        private val token = TokenProvider.getTelegramToken()
        private val telegramClient: TelegramClient = OkHttpTelegramClient(token)

        fun getClient() = telegramClient
    }
}

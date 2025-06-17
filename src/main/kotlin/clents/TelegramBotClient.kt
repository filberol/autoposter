package ru.social.ai.clents

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient
import org.telegram.telegrambots.meta.generics.TelegramClient
import ru.social.ai.util.TokenProvider

class TelegramBotClient {
    companion object {
        private val botToken = TokenProvider.getToken()
        private val telegramClient: TelegramClient = OkHttpTelegramClient(botToken)
        fun getClient() = telegramClient
    }
}

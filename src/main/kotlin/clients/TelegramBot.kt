package ru.social.ai.clients

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.slf4j.LoggerFactory
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient
import ru.social.ai.util.TokenProvider

object TelegramBot {
    val client: OkHttpTelegramClient by lazy { getHttpClient() }
    private val logger = LoggerFactory.getLogger(this::class.java)


    private val token = TokenProvider.getTelegramToken()
    private var customLogger: HttpLoggingInterceptor.Logger =
        HttpLoggingInterceptor.Logger { message: String ->
            logger.debug(message)
        }
    private val loggingInterceptor = HttpLoggingInterceptor(customLogger)
        .also {
            it.level = HttpLoggingInterceptor.Level.BODY
        }
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor).build()

    private fun getHttpClient() = OkHttpTelegramClient(httpClient, token)
}

package ru.social.ai.clients

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.slf4j.LoggerFactory
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.*
import org.telegram.telegrambots.meta.api.objects.message.Message

object TelegramBot {
    val client: OkHttpTelegramClient by lazy { getHttpClient() }
    private val logger = LoggerFactory.getLogger(this::class.java)

    private val token = System.getenv("telegram_api_token")
    private var customLogger: HttpLoggingInterceptor.Logger =
        HttpLoggingInterceptor.Logger { message: String ->
            if (message.contains("-->") ||
                message.contains("<--") ||
                message.contains("{\"")) {
                logger.debug(message)
            }
        }
    private val loggingInterceptor = HttpLoggingInterceptor(customLogger)
        .also {
            it.level = HttpLoggingInterceptor.Level.BODY
        }
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor).build()

    private fun getHttpClient() = OkHttpTelegramClient(httpClient, token)
}

/**
 * Adapter method to execute abstract partial method if it's really an instance
 * Strange telegrambots doesn't have this generic
 */
fun OkHttpTelegramClient.execute(sendMessage: PartialBotApiMethod<Message>) {
    when (sendMessage) {
        is SendMessage -> execute(sendMessage)
        is SendPhoto -> execute(sendMessage)
        is SendVideo -> execute(sendMessage)
        is SendDocument -> execute(sendMessage)
        is SendVoice -> execute(sendMessage)
        is SendMediaGroup -> execute(sendMessage)
        is SendAnimation -> execute(sendMessage)
        else -> throw RuntimeException("Unsupported message type in adapter ${sendMessage.javaClass.canonicalName}")
    }
}

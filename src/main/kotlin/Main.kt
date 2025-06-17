package ru.social.ai

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import ru.social.ai.consumers.BasicConsumer
import ru.social.ai.util.TokenProvider


fun main() {
    try {
        val botToken = TokenProvider.getToken()
        val botsApplication = TelegramBotsLongPollingApplication()
        botsApplication.registerBot(botToken, BasicConsumer())
    } catch (e: TelegramApiException) {
        e.printStackTrace()
    }
}

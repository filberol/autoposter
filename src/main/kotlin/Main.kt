package ru.social.ai

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication
import org.telegram.telegrambots.longpolling.exceptions.TelegramApiErrorResponseException
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import ru.social.ai.consumers.BasicConsumer
import ru.social.ai.util.TokenProvider
import java.lang.NullPointerException
import kotlin.system.exitProcess


fun main() {
    try {
        val botToken = TokenProvider.getTelegramToken()
        val botsApplication = TelegramBotsLongPollingApplication()
        botsApplication.registerBot(botToken, BasicConsumer())
    } catch (e: TelegramApiException) {
        e.printStackTrace()
    } catch (e: NullPointerException) {
        println("Need environment variables")
        exitProcess(1)
    } catch (e: TelegramApiErrorResponseException) {
        e.printStackTrace()
    }
}

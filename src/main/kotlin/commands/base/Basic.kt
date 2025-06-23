package ru.social.ai.commands.base

import ru.social.ai.clients.ChatBot
import ru.social.ai.clients.TelegramBot

abstract class Basic: Command {
    abstract val triggerName: String

    val telegramClient = TelegramBot.getClient()
    val chatClient = ChatBot.getRelevantClient()
}

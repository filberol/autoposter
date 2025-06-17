package ru.social.ai.commands

import ru.social.ai.clents.TelegramBot
import ru.social.ai.clients.ChatBot

abstract class Basic: Command {
    val telegramClient = TelegramBot.getClient()
    val chatClient = ChatBot.getRelevantClient()
}

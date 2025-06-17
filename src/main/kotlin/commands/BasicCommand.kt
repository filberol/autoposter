package ru.social.ai.commands

import ru.social.ai.clents.TelegramBotClient

abstract class BasicCommand: Command {
    val client = TelegramBotClient.getClient()
}

package ru.social.ai.commands

import org.telegram.telegrambots.meta.api.objects.Update

interface Command {
    val name: String
    fun execute(update: Update)
}

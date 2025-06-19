package ru.social.ai.commands

import org.telegram.telegrambots.meta.api.objects.Update

interface Command {
    /**
     *  Telegram command name to trigger execution
     */
    fun triggerName(): String = ";;;;not_registrable_command;;;" // Override here to never register such

    /**
     * Asynchronous function to invoke commands
     */
    suspend fun execute(update: Update)

    /**
     * Decides if the command should be custom executed by predicate
     */
    fun customTrigger(update: Update): Boolean = false


}

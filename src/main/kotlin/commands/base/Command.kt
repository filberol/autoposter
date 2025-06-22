package ru.social.ai.commands.base

import org.telegram.telegrambots.meta.api.objects.Update

interface Command {
    /**
     *  Telegram command name to trigger execution
     */
    fun triggerName(): String = ";;;;not_registrable_command;;;" // Override here to never register such

    /**
     * Asynchronous function to invoke commands
     */
    suspend fun execute(update: Update) = Unit
    suspend fun execute(updates: List<Update>) = Unit
    suspend fun sizeOverrideExecute(updates: List<Update>) =
        if (updates.size == 1) execute(updates.first()) else execute(updates)

    /**
     * Decides if the command should be custom executed by predicate
     */
    fun customTrigger(update: Update): Boolean = false
    fun customTrigger(updates: List<Update>): Boolean = false
}

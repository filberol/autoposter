package ru.social.ai.commands.base

import org.telegram.telegrambots.meta.api.objects.Update

interface Command {
    /**
     * Asynchronous function to invoke commands
     */
    suspend fun execute(update: Update) = Unit
    suspend fun execute(updates: List<Update>) = Unit

    /**
     * Adapter for multi-message commands e.g. multiple attachments
     */
    suspend fun sizeOverrideExecute(updates: List<Update>) =
        if (updates.size == 1) execute(updates.first()) else execute(updates)

    /**
     * Decides if the command should be custom executed by predicate
     */
    fun customTrigger(update: Update): Boolean = false
    fun customTrigger(updates: List<Update>): Boolean = false

    /**
     * Filter command execution permissions
     */
    fun filterChain(update: Update): Boolean = true
}

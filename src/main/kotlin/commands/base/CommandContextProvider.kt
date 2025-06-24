package ru.social.ai.commands.base

object CommandContextProvider {
    private val userContexts = mutableMapOf<Long, CommandContext>()

    fun getOrCreate(userId: Long): CommandContext =
        userContexts.getOrPut(userId) { CommandContext() }

    fun clear(userId: Long) {
        userContexts.remove(userId)
    }
}

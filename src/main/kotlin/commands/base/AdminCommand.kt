package ru.social.ai.commands.base

import org.telegram.telegrambots.meta.api.objects.Update

abstract class AdminCommand : Basic() {
    private val admins: List<Long> = System.getenv("admin_ids")
        ?.split(',')
        ?.map { it.toLong() }
        ?: emptyList()

    override fun filterChain(update: Update) =
        admins.contains(update.message.from.id)
}

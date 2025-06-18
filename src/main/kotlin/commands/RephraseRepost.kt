package ru.social.ai.commands

import org.telegram.telegrambots.meta.api.objects.Update

class RephraseRepost: Basic() {
    override fun customTrigger(update: Update): Boolean {
        return super.customTrigger(update)
    }

    override suspend fun execute(update: Update) {
        TODO("Not yet implemented")
    }
}

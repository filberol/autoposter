package ru.social.ai.consumers

import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer
import org.telegram.telegrambots.meta.api.objects.Update

class BasicConsumer: LongPollingSingleThreadUpdateConsumer {
    private val dispatcher = CommandDispatcher()

    override fun consume(update: Update) {
        dispatcher.dispatchCommand(update)
    }
}

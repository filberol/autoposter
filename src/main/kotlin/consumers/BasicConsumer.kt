package ru.social.ai.consumers

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer
import org.telegram.telegrambots.meta.api.objects.Update

class BasicConsumer: LongPollingSingleThreadUpdateConsumer {
    private val dispatcher = CommandDispatcher()
    private val mediaCollector = MediaGroupCollector(
        scope = CoroutineScope(Dispatchers.Default),
        onUpdatesReady = { updates -> dispatcher.dispatchCommands(updates) }
    )

    override fun consume(update: Update) {
        mediaCollector.processUpdate(update)
    }
}

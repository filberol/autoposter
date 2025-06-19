package ru.social.ai.consumers

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer
import org.telegram.telegrambots.meta.api.objects.Update

private const val CommandExecutionTimeoutMs = 30000L

class BasicConsumer: LongPollingSingleThreadUpdateConsumer {
    private val dispatcher = CommandDispatcher()
    private val mediaCollector = MediaGroupCollector(
        scope = CoroutineScope(Dispatchers.Default),
        onUpdatesReady = { updates -> dispatchImmediately(updates) },
    )

    override fun consume(update: Update) {
        mediaCollector.processUpdate(update)
    }

    private fun dispatchImmediately(updates: List<Update>) {
        runBlocking {
            withTimeout(CommandExecutionTimeoutMs) {
                dispatcher.dispatchCommands(updates)
            }
        }
    }
}

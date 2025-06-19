package ru.social.ai.consumers

import kotlinx.coroutines.*
import org.telegram.telegrambots.meta.api.objects.Update
import java.util.concurrent.ConcurrentHashMap

class MediaGroupCollector(
    private val scope: CoroutineScope,
    private val onUpdatesReady: (List<Update>) -> Unit
) {
    private val mediaGroups = ConcurrentHashMap<String, MutableList<Update>>()
    private val jobs = ConcurrentHashMap<String, Job>()

    fun processUpdate(update: Update) {
        val message = update.message ?: run {
            dispatchImmediately(update)
            return
        }

        val mediaGroupId = message.mediaGroupId
        if (mediaGroupId == null) {
            dispatchImmediately(update)
            return
        }

        val chatId = message.chatId.toString()
        val groupKey = "$chatId:$mediaGroupId"

        mediaGroups.compute(groupKey) { _, list ->
            val newList = list ?: mutableListOf()
            newList.add(update)

            // Schedule processing only for the first message in group
            if (newList.size == 1) {
                scheduleGroupProcessing(groupKey)
            }
            newList
        }
    }

    private fun scheduleGroupProcessing(groupKey: String) {
        jobs[groupKey] = scope.launch {
            delay(1000) // Wait for 1 second to collect group messages

            val groupUpdates = mediaGroups.remove(groupKey)
            jobs.remove(groupKey)

            groupUpdates?.let { updates ->
                // Sort by message ID to maintain original order
                val sortedUpdates = updates.sortedBy { it.message.messageId }
                onUpdatesReady(sortedUpdates)
            }
        }
    }

    private fun dispatchImmediately(update: Update) {
        onUpdatesReady(listOf(update))
    }

    fun cancelAll() {
        jobs.values.forEach { it.cancel() }
        mediaGroups.clear()
        jobs.clear()
    }
}

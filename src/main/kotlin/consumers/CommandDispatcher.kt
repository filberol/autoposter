package ru.social.ai.consumers

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import ru.social.ai.clents.TelegramBot
import ru.social.ai.commands.*
import ru.social.ai.exceptions.UserReasonableException
import ru.social.ai.util.UpdateExtractor
import java.util.concurrent.ExecutionException


class CommandDispatcher {

    private val responseClient = TelegramBot.getClient()

    companion object {
        private val registeredCommands = listOf(
            Test(),
            ReplyDirectlyWithAi(),
            RephraseRepost()
        )
    }

    suspend fun dispatchCommands(updates: List<Update>) {
        val foundCommand = if (updates.size == 1) {
            registeredCommands
                .firstOrNull {
                    it.customTrigger(updates.first())
                            || UpdateExtractor.isTextPresent(updates.first())
                            && UpdateExtractor.extractText(updates.first()).startsWith(it.triggerName())

                }
        } else {
            registeredCommands.firstOrNull {
                it.customTrigger(updates)
            }
        }
        println("Executing command $foundCommand")
        try {
            foundCommand?.let { foundCommand.sizeOverrideExecute(updates) }
            if (foundCommand == null) {
                NotFound().sizeOverrideExecute(updates)
            }
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
            responseClient.execute(
                SendMessage(
                    updates.first().message.chatId.toString(), "Непредвиденная ошибка"
                )
            )
        } catch (e: UserReasonableException) {
            responseClient.execute(
                SendMessage(
                    updates.first().message.chatId.toString(), e.message.toString()
                )
            )
        }
    }
}

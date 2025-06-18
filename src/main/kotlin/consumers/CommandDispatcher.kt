package ru.social.ai.consumers

import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import ru.social.ai.clents.TelegramBot
import ru.social.ai.commands.*
import ru.social.ai.exceptions.UserReasonableException
import ru.social.ai.util.UpdateExtractor
import java.util.concurrent.ExecutionException

private const val CommandExecutionTimeoutMs = 30000L

class CommandDispatcher {

    private val responseClient = TelegramBot.getClient()

    companion object {
        private val registeredCommands = listOf(
            Test(),
            ReplyDirectlyWithAi(),
            RephraseRepost()
        )
    }

    suspend fun dispatchCommand(update: Update) {
        val foundCommand: Command? = registeredCommands
            .firstOrNull {
                UpdateExtractor.isTextPresent(update)
                        && UpdateExtractor.extractText(update).startsWith(it.triggerName())
                        || it.customTrigger(update)
            }
        withTimeout(CommandExecutionTimeoutMs) {
            launch {
                try {
                    foundCommand?.let { foundCommand.execute(update) }
                    if (foundCommand == null) {
                        NotFound().execute(update)
                    }
                } catch (e: TelegramApiException) {
                    e.printStackTrace()
                } catch (e: ExecutionException) {
                    e.printStackTrace()
                    responseClient.execute(
                        SendMessage(
                            update.message.chatId.toString(), "Непредвиденная ошибка"
                        )
                    )
                } catch (e: UserReasonableException) {
                    responseClient.execute(
                        SendMessage(
                            update.message.chatId.toString(), e.message.toString()
                        )
                    )
                }
            }
        }
    }
}

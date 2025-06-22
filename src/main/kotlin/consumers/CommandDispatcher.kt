package ru.social.ai.consumers

import org.slf4j.LoggerFactory
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import ru.social.ai.clents.TelegramBot
import ru.social.ai.commands.*
import ru.social.ai.commands.common.Debug
import ru.social.ai.commands.common.Start
import ru.social.ai.commands.common.Test
import ru.social.ai.exceptions.UserReasonableException
import ru.social.ai.util.UpdateExtractor
import java.util.concurrent.ExecutionException


class CommandDispatcher {
    private val responseClient = TelegramBot.getClient()
    private val logger = LoggerFactory.getLogger(CommandDispatcher::class.java)

    companion object {
        private val registeredCommands = listOf(
            Test(),
            ReplyDirectlyWithAi(),
            RephraseRepost(),
            Start(),
            Debug()
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
        logger.debug("Executing command {}", foundCommand)
        try {
            foundCommand?.let { foundCommand.sizeOverrideExecute(updates) }
            if (foundCommand == null) {
                NotFound().sizeOverrideExecute(updates)
            }
        } catch (e: TelegramApiException) {
            logger.error(e.message)
        } catch (e: ExecutionException) {
            logger.error(e.message)
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
        } catch (e: NullPointerException) {
            logger.error(e.message)
        }
    }
}

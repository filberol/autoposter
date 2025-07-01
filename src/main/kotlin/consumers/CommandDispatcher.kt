package ru.social.ai.consumers

import org.slf4j.LoggerFactory
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import ru.social.ai.clients.TelegramBot
import ru.social.ai.commands.*
import ru.social.ai.commands.base.MultiStage
import ru.social.ai.commands.common.*
import ru.social.ai.commands.debug.DebugUpdate
import ru.social.ai.commands.debug.ListClientSubscriptions
import ru.social.ai.commands.debug.ProcessConfigurationSources
import ru.social.ai.commands.debug.ReplyDirectlyWithAi
import ru.social.ai.commands.setup.*
import ru.social.ai.db.entities.UserCommandStageEntity
import ru.social.ai.exceptions.UserReasonableException
import ru.social.ai.util.extractTextIfPresent
import java.util.concurrent.ExecutionException

class CommandDispatcher {
    private val responseClient = TelegramBot.client
    private val logger = LoggerFactory.getLogger(this::class.java)

    companion object {
        private val cancel = Cancel("/cancel")

        private val registeredCommands = listOf(
            Test("/test"),
            ReplyDirectlyWithAi("/reply"),
            RephraseRepost("/rephrase"),
            Start("/start"),
            DebugUpdate("/debug"),
            MassMailing("/mass"),
            ProcessConfigurationSources("/processSources"),
            ListClientSubscriptions("/listSubscriptions"),
            object : MultiStage("/setup") {
                override val stages = listOf(SetupI, SetupII, SetupIII, SetupIV)
            }
        )
    }

    suspend fun dispatchCommands(updates: List<Update>) {
        if (updates.first().message == null) return
        val commandInProcess = UserCommandStageEntity.findById(updates.first().message.from.id)
        val text = updates.first().extractTextIfPresent()
        val command = commandInProcess?.toCommandStage()?.commandName ?: text ?: ""

        val foundCommand = if (text?.startsWith(cancel.triggerName) == true) {
            cancel
        } else if (updates.size == 1) {
            registeredCommands
                .firstOrNull {
                    it.customTrigger(updates.first())
                            || command.startsWith(it.triggerName)
                }
        } else {
            registeredCommands.firstOrNull {
                it.customTrigger(updates)
            }
        }
        logger.debug("Executing command {}", foundCommand?.triggerName)
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

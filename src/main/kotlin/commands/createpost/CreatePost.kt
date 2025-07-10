package ru.social.ai.commands.createpost

import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.objects.Update
import ru.social.ai.ai.SimpleDialog
import ru.social.ai.commands.base.*
import ru.social.ai.commands.base.callback.CallbackOriginator
import ru.social.ai.db.entities.ChannelConfigurations
import ru.social.ai.db.entities.ChannelConfigurations.linkId
import ru.social.ai.db.entities.ChannelConfigurations.owner
import ru.social.ai.db.entities.toChannelConfiguration
import ru.social.ai.prebuilders.SendMessagePreBuilder
import ru.social.ai.util.*

class CreatePost(override val triggerName: String) : CallbackOriginator() {

    override suspend fun execute(update: Update) {
        // Get chanel config
        val channelLink = update.extractTextWithoutCommand().trim()
        val channelInfo = getAdministratedChannelFromLink(channelLink)
        val config = ChannelConfigurations.select {
            (owner eq update.message.from.id) and
                    (linkId eq channelInfo.id)
        }.map { it.toChannelConfiguration() }.first()

        // Get message compilation
        val messages = config.informationSources
            .map { getLastMessageFromChannelLink(it) }
        val messageTexts = messages.map { it.content.extractText() }

        // Receive compilation response
        val promptBody = messageTexts.joinToString("\n-------------\n")
        val response = SimpleDialog.getPostCompilationWithDecision(promptBody)

        // Retrieve original attachment if needed and send
        val attachment = messages[response.first - 1].downloadImageIfPresent()

        val sendMessage = if (attachment != null) {
            SendPhoto.builder()
                .chatId(update.message.chatId.toString())
                .photo(attachment)
                .caption(response.second)
                .buildCallbacks()
                .build() as SendPhoto
        } else {
            SendMessagePreBuilder
                .chatId(update.message.chatId)
                .text(response.second)
                .buildCallbacks()
                .build()
        }
        context.set("message", sendMessage)
        telegramClient.execute(sendMessage)
    }
}


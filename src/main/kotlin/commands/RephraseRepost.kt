package ru.social.ai.commands

import org.telegram.telegrambots.meta.api.methods.send.*
import org.telegram.telegrambots.meta.api.objects.Update
import ru.social.ai.ai.SimpleDialog
import ru.social.ai.commands.base.Basic
import ru.social.ai.prebuilders.SendMessagePreBuilder
import ru.social.ai.util.*

class RephraseRepost(
    override val triggerName: String,
) : Basic() {
    override fun customTrigger(updates: List<Update>) = updates.first().isReposted()
    override fun customTrigger(update: Update) = update.isReposted()

    override suspend fun execute(update: Update) {
        val mediaBuilder = update.extractAttachmentMediaBuilder()
        val mediaFile = update.extractAttachmentInputFile()
        val text = update.extractTextIfPresent()
        val response = text?.let { SimpleDialog.getRephrase(it) }
        mediaBuilder?.let { mediaBuilder.caption(response) }
        if (mediaBuilder != null) {
                mediaBuilder.build().sendWithCaption(mediaFile!!, update.message.chatId.toString())
        } else {
            response?.let {
                telegramClient.execute(
                    SendMessagePreBuilder.chatId(update.message.chatId.toString()).text(response).build()
                )
            }
        }

    }

    override suspend fun execute(updates: List<Update>) {
        val mediaBuilders = updates.mapNotNull { it.extractAttachmentMediaBuilder() }
        val text = updates.first().extractTextIfPresent()
        val response = text?.let { SimpleDialog.getRephrase(it) }
        response.let { mediaBuilders.first().caption(response) }
        val sendMedia = SendMediaGroup.builder()
            .chatId(updates.first().message.chatId.toString())
            .medias(mediaBuilders.map { it.build() })
            .build()
        telegramClient.execute(sendMedia)
    }
}

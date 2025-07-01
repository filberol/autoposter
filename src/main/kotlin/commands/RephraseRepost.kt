package ru.social.ai.commands

import org.telegram.telegrambots.meta.api.methods.send.*
import org.telegram.telegrambots.meta.api.objects.Update
import ru.social.ai.ai.SimpleDialog
import ru.social.ai.commands.base.Basic
import ru.social.ai.prebuilders.SendMessagePreBuilder
import ru.social.ai.util.TextExtractor.extractAttachmentInputFile
import ru.social.ai.util.TextExtractor.extractAttachmentMediaBuilder
import ru.social.ai.util.TextExtractor.extractTextIfPresent
import ru.social.ai.util.isReposted
import ru.social.ai.util.sendWithCaption

class RephraseRepost(
    override val triggerName: String,
) : Basic() {
    override fun customTrigger(updates: List<Update>) = updates.first().isReposted()
    override fun customTrigger(update: Update) = update.isReposted()

    override suspend fun execute(update: Update) {
        val mediaBuilder = extractAttachmentMediaBuilder(update)
        val mediaFile = extractAttachmentInputFile(update)
        val text = extractTextIfPresent(update)
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
        val mediaBuilders = updates.mapNotNull { extractAttachmentMediaBuilder(it) }
        val text = extractTextIfPresent(updates.first())
        val response = text?.let { SimpleDialog.getRephrase(it) }
        response.let { mediaBuilders.first().caption(response) }
        val sendMedia = SendMediaGroup.builder()
            .chatId(updates.first().message.chatId.toString())
            .medias(mediaBuilders.map { it.build() })
            .build()
        telegramClient.execute(sendMedia)
    }
}

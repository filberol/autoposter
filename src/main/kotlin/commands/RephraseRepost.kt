package ru.social.ai.commands

import org.telegram.telegrambots.meta.api.methods.send.*
import org.telegram.telegrambots.meta.api.objects.Update
import ru.social.ai.ai.prompts.SimpleDialog
import ru.social.ai.commands.base.Basic
import ru.social.ai.util.MediaUtils
import ru.social.ai.util.UpdateExtractor

class RephraseRepost : Basic() {
    override fun triggerName() = "/rephrase"
    override fun customTrigger(updates: List<Update>) = UpdateExtractor.isReposted(updates.first())
    override fun customTrigger(update: Update) = UpdateExtractor.isReposted(update)

    override suspend fun execute(update: Update) {
        val mediaBuilder = UpdateExtractor.extractAttachmentMediaBuilder(update)
        val mediaFile = UpdateExtractor.extractAttachmentInputFile(update)
        val text = UpdateExtractor.extractTextIfPresent(update)
        val response = text?.let { SimpleDialog.getRephrase(it) }
        mediaBuilder?.let { mediaBuilder.caption(response) }
        if (mediaBuilder != null) {
            MediaUtils.sendMediaWithCaption(
                mediaBuilder.build(), mediaFile!!, update.message.chatId.toString()
            )
        } else {
            response?.let {
                telegramClient.execute(
                    SendMessage.builder().chatId(update.message.chatId.toString()).text(response).build()
                )
            }
        }

    }

    override suspend fun execute(updates: List<Update>) {
        val mediaBuilders = updates.mapNotNull { UpdateExtractor.extractAttachmentMediaBuilder(it) }
        val text = UpdateExtractor.extractTextIfPresent(updates.first())
        val response = text?.let { SimpleDialog.getRephrase(it) }
        response.let { mediaBuilders.first().caption(response) }
        val sendMedia = SendMediaGroup.builder()
            .chatId(updates.first().message.chatId.toString())
            .medias(mediaBuilders.map { it.build() })
            .build()
        telegramClient.execute(sendMedia)
    }
}

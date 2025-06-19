package ru.social.ai.commands

import org.telegram.telegrambots.meta.api.methods.send.*
import org.telegram.telegrambots.meta.api.objects.Update
import ru.social.ai.util.MediaUtils
import ru.social.ai.util.UpdateExtractor

class RephraseRepost : Basic() {
    override fun triggerName() = "/rephrase"
    override fun customTrigger(updates: List<Update>) = UpdateExtractor.isReposted(updates.first())
    override fun customTrigger(update: Update) = UpdateExtractor.isReposted(update)

    override suspend fun execute(update: Update) {
        val mediaBuilder = UpdateExtractor.extractAttachmentMediaBuilder(update)
        val mediaFile = UpdateExtractor.extractAttachmentInputFile(update)
        mediaBuilder ?: return; mediaFile ?: return
        mediaBuilder.caption(UpdateExtractor.extractText(update))
        MediaUtils.sendMediaWithCaption(mediaBuilder.build(), mediaFile, update.message.chatId.toString())
    }

    override suspend fun execute(updates: List<Update>) {
        val mediaBuilders = updates.mapNotNull { UpdateExtractor.extractAttachmentMediaBuilder(it) }
        mediaBuilders.first().caption(UpdateExtractor.extractTextIfPresent(updates.first()))
        val sendMedia = SendMediaGroup.builder()
            .chatId(updates.first().message.chatId.toString())
            .medias(mediaBuilders.map { it.build() })
            .build()
        telegramClient.execute(sendMedia)
    }
}

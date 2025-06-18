package ru.social.ai.commands

import io.github.sashirestela.openai.domain.chat.ChatMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.*
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.media.InputMedia
import org.telegram.telegrambots.meta.api.objects.media.InputMediaDocument
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto
import org.telegram.telegrambots.meta.api.objects.media.InputMediaVideo
import org.telegram.telegrambots.meta.api.objects.message.Message
import ru.social.ai.ai.prompts.BasicRephrasePrompt
import ru.social.ai.prebuilders.FreeModelPreBuilder
import ru.social.ai.util.UpdateExtractor
import ru.social.ai.util.UpdateExtractor.Companion.extractAttachments
import java.util.concurrent.ConcurrentHashMap

class RephraseRepost : Basic() {
    private val mediaGroupCache = ConcurrentHashMap<String, MutableList<Message>>()
    override fun triggerName() = "/rephrase"
    override fun customTrigger(update: Update) = UpdateExtractor.isReposted(update)

    override suspend fun execute(update: Update) {
        val attachments = extractAttachments(update)
        val inputText = if (UpdateExtractor.isReposted(update)) {
            UpdateExtractor.extractText(update)
        } else {
            UpdateExtractor.extractTextWithoutCommand(update)
        }
        val messages = listOf(
            ChatMessage.SystemMessage.of(BasicRephrasePrompt),
            ChatMessage.UserMessage.of(inputText)
        )

        val response = chatClient.chatCompletions().create(
            FreeModelPreBuilder
                .messages(messages)
                .build()
        )

        val rephrasedText = withContext(Dispatchers.IO) {
            response.get().choices.first().message.content
        }.also { println("Rephrased: $it") }

        ////////////////////

    }

    private fun handleMediaGroup(message: Message, mediaGroupId: String) {
        mediaGroupCache.computeIfAbsent(mediaGroupId) { mutableListOf() }.add(message)

        // Process after short delay to allow group to complete
        Thread.sleep(500)

        val group = mediaGroupCache[mediaGroupId] ?: return
        if (group.size >= 2) { // Minimum 2 items for a group
            replyToMediaGroup(group.sortedBy { it.messageId })
            mediaGroupCache.remove(mediaGroupId)
        }
    }

    private fun replyToMediaGroup(messages: List<Message>): SendMediaGroup? {
        val mediaList = mutableListOf<InputMedia>()

        for (msg in messages) {
            when {
                msg.hasPhoto() -> {
                    InputMediaPhoto.builder()
                        .media(msg.photo.last().fileId)
                        .caption(msg.caption)
                        .build().also { mediaList.add(it) }
                }
                msg.hasVideo() -> {
                    InputMediaVideo.builder()
                        .media(msg.video.fileId)
                        .caption(msg.caption)
                        .build().also { mediaList.add(it) }
                }
                msg.hasDocument() -> {
                    InputMediaDocument.builder()
                        .media(msg.document.fileId)
                        .caption(msg.caption)
                        .build().also { mediaList.add(it) }
                }
            }
        }

        if (mediaList.isEmpty()) return null

        return SendMediaGroup.builder()
            .chatId(messages.first().chatId.toString())
            .medias(mediaList)
            .build()
    }

    private fun replyToSingleMessage(original: Message): BotApiMethod<*> {
        return when {
            original.hasText() -> SendMessage.builder()
                .chatId(original.chatId.toString())
                .text(original.text)
                .build()
            original.hasPhoto() -> SendPhoto.builder()
                .chatId(original.chatId.toString())
                .photo(InputFile(original.photo.last().fileId))
                .caption(original.text)
                .build()
            original.hasVideo() -> SendVideo.builder()
                .chatId(original.chatId.toString())
                .video(InputFile(original.video.fileId))
                .caption(original.text)
                .build()
            original.hasDocument() -> SendDocument.builder()
                .chatId(original.chatId.toString())
                .document(InputFile(original.document.fileId))
                .caption(original.text)
                .build()
            else -> null
        }
    }
}

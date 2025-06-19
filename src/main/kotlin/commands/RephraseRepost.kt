package ru.social.ai.commands

import io.github.sashirestela.openai.domain.chat.ChatMessage
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod
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
import java.util.concurrent.ConcurrentHashMap

class RephraseRepost : Basic() {
    private val mediaGroupCache = ConcurrentHashMap<String, MutableList<Message>>()
    override fun triggerName() = "/rephrase"
    override fun customTrigger(update: Update) = UpdateExtractor.isReposted(update)

    override suspend fun execute(update: Update) {
//        val attachments = extractAttachments(update)
        val message = update.message
        val inputText = if (UpdateExtractor.isReposted(update)) {
            UpdateExtractor.extractTextIfPresent(update).also { println(it) }
        } else {
            UpdateExtractor.extractTextWithoutCommand(update)
        }
//        val messages = listOf(
//            ChatMessage.SystemMessage.of(BasicRephrasePrompt),
//            ChatMessage.UserMessage.of(inputText)
//        )
//
//        val response = chatClient.chatCompletions().create(
//            FreeModelPreBuilder
//                .messages(messages)
//                .build()
//        )

//        val rephrasedText = withContext(Dispatchers.IO) {
//            response.get().choices.first().message.content
//        }.also { println("Rephrased: $it") }

        val mediaGroupId = message.mediaGroupId

        if (mediaGroupId != null) {
            handleMediaGroup(message, mediaGroupId)?.let { telegramClient.execute(it) }
        } else {
            handleSingleMessage(message)
        }

    }

    private fun handleMediaGroup(message: Message, mediaGroupId: String): SendMediaGroup? {
        mediaGroupCache.computeIfAbsent(mediaGroupId) { mutableListOf() }.add(message)
        val mediaList = mutableListOf<InputMedia>()

        val group = mediaGroupCache[mediaGroupId] ?: return null
        if (group.size >= 2) { // Minimum 2 items for a group
            for (msg in group.sortedBy { it.messageId }) {
                when {
                    msg.hasPhoto() -> {
                        InputMediaPhoto.builder()
                            .media(msg.photo.last().fileId)
                            .caption("msg.caption")
                            .build().also { mediaList.add(it) }
                    }

                    msg.hasVideo() -> {
                        InputMediaVideo.builder()
                            .media(msg.video.fileId)
                            .caption("msg.caption")
                            .build().also { mediaList.add(it) }
                    }

                    msg.hasDocument() -> {
                        InputMediaDocument.builder()
                            .media(msg.document.fileId)
                            .caption("msg.caption")
                            .build().also { mediaList.add(it) }
                    }
                }
            }
            if (mediaList.isEmpty()) return null

            return SendMediaGroup.builder()
                .chatId(group.first().chatId.toString())
                .medias(mediaList)
                .build()
        } else return null
    }

    private fun handleSingleMessage(original: Message) {
        when {
            original.hasPhoto() -> SendPhoto.builder()
                .chatId(original.chatId.toString())
                .photo(InputFile(original.photo.last().fileId))
                .caption("original.text")
                .build().also { telegramClient.execute(it) }

            original.hasVideo() -> SendVideo.builder()
                .chatId(original.chatId.toString())
                .video(InputFile(original.video.fileId))
                .caption("original.text")
                .build().also { telegramClient.execute(it) }

            original.hasDocument() -> SendDocument.builder()
                .chatId(original.chatId.toString())
                .document(InputFile(original.document.fileId))
                .caption("original.text")
                .build().also { telegramClient.execute(it) }
        }
    }
}

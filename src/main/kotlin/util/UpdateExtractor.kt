package ru.social.ai.util

import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.media.InputMedia.InputMediaBuilder
import org.telegram.telegrambots.meta.api.objects.media.InputMediaAnimation
import org.telegram.telegrambots.meta.api.objects.media.InputMediaAudio
import org.telegram.telegrambots.meta.api.objects.media.InputMediaDocument
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto
import org.telegram.telegrambots.meta.api.objects.media.InputMediaVideo
import ru.social.ai.exceptions.TextNotProvided

class UpdateExtractor {
    companion object {
        fun extractText(update: Update): String {
            val message = update.message ?: throw TextNotProvided()
            val inputText = when {
                message.hasText() -> message.text
                message.hasPhoto() && message.caption != null -> message.caption
                message.hasVideo() && message.caption != null -> message.caption
                else -> throw TextNotProvided()
            }
            return inputText
        }

        fun extractTextWithoutCommand(update: Update): String {
            val text = extractText(update)
            return text.split(" ", limit = 2).getOrElse(1) { throw TextNotProvided() }
        }

        fun extractTextIfPresent(update: Update): String? {
            return try { extractText(update) } catch (e: TextNotProvided) { null }
        }

        fun isTextPresent(update: Update): Boolean {
            return try {
                extractText(update)
                true
            } catch (e: TextNotProvided) {
                false
            }
        }

        fun isReposted(update: Update): Boolean {
            return update.message?.let { message ->
                message.forwardFrom != null ||
                        message.forwardFromChat != null ||
                        message.forwardFromMessageId != null ||
                        message.forwardSenderName != null ||
                        message.forwardSignature != null ||
                        message.forwardDate != null
            } ?: false
        }

        fun extractAttachmentInputFile(update: Update): InputFile? {
            val message = update.message
            return when {
                message.hasPhoto() -> InputFile(message.photo.last().fileId)
                message.hasVideo() -> InputFile(message.video.fileId)
                message.hasDocument() -> InputFile(message.document.fileId)
                message.hasAnimation() -> InputFile(message.animation.fileId)
                message.hasAudio() -> InputFile(message.audio.fileId)
                else -> null
            }
        }

        fun extractAttachmentMediaBuilder(update: Update): InputMediaBuilder<*,*>? {
            val message = update.message
            return when {
                message.hasPhoto() -> InputMediaPhoto.builder().media(message.photo.last().fileId)
                message.hasVideo() -> InputMediaVideo.builder().media(message.video.fileId)
                message.hasDocument() -> InputMediaDocument.builder().media(message.document.fileId)
                message.hasAnimation() -> InputMediaAnimation.builder().media(message.animation.fileId)
                message.hasAudio() -> InputMediaAudio.builder().media(message.audio.fileId)
                else -> null
            }
        }
    }
}

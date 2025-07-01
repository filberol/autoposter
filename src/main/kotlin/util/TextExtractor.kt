package ru.social.ai.util

import it.tdlight.jni.TdApi.MessageContent
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.media.InputMedia.InputMediaBuilder
import org.telegram.telegrambots.meta.api.objects.media.InputMediaAnimation
import org.telegram.telegrambots.meta.api.objects.media.InputMediaAudio
import org.telegram.telegrambots.meta.api.objects.media.InputMediaDocument
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto
import org.telegram.telegrambots.meta.api.objects.media.InputMediaVideo
import ru.social.ai.exceptions.TextNotProvided

fun Update.extractText(): String {
    val message = this.message ?: throw TextNotProvided()
    val inputText = when {
        message.hasText() -> message.text
        message.hasPhoto() && message.caption != null -> message.caption
        message.hasVideo() && message.caption != null -> message.caption
        else -> throw TextNotProvided()
    }
    return inputText
}

fun Update.extractTextWithoutCommand(): String {
    val text = this.extractText()
    return text.split(" ", limit = 2).getOrElse(1) { throw TextNotProvided() }
}

fun Update.extractTextIfPresent(): String? {
    return try { this.extractText() } catch (e: TextNotProvided) { null }
}

fun Update.isTextPresent(): Boolean {
    return try {
        this.extractText()
        true
    } catch (e: TextNotProvided) {
        false
    }
}

fun Update.extractAttachmentInputFile(): InputFile? {
    val message = this.message
    return when {
        message.hasPhoto() -> InputFile(message.photo.last().fileId)
        message.hasVideo() -> InputFile(message.video.fileId)
        message.hasDocument() -> InputFile(message.document.fileId)
        message.hasAnimation() -> InputFile(message.animation.fileId)
        message.hasAudio() -> InputFile(message.audio.fileId)
        else -> null
    }
}

fun Update.extractAttachmentMediaBuilder(): InputMediaBuilder<*,*>? {
    val message = this.message
    return when {
        message.hasPhoto() -> InputMediaPhoto.builder().media(message.photo.last().fileId)
        message.hasVideo() -> InputMediaVideo.builder().media(message.video.fileId)
        message.hasDocument() -> InputMediaDocument.builder().media(message.document.fileId)
        message.hasAnimation() -> InputMediaAnimation.builder().media(message.animation.fileId)
        message.hasAudio() -> InputMediaAudio.builder().media(message.audio.fileId)
        else -> null
    }
}

fun MessageContent.extractText(update: Update): String? {
    return ""
}

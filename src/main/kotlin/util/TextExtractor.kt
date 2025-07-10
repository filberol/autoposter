package ru.social.ai.util

import it.tdlight.jni.TdApi
import it.tdlight.jni.TdApi.MessageContent
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.media.InputMedia.InputMediaBuilder
import org.telegram.telegrambots.meta.api.objects.media.InputMediaAnimation
import org.telegram.telegrambots.meta.api.objects.media.InputMediaAudio
import org.telegram.telegrambots.meta.api.objects.media.InputMediaDocument
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto
import org.telegram.telegrambots.meta.api.objects.media.InputMediaVideo
import org.telegram.telegrambots.meta.api.objects.message.Message
import ru.social.ai.exceptions.TextNotProvided

fun Update.extractText(): String {
    val message = this.message ?: throw TextNotProvided()
    val inputText = when {
        message.hasText() -> message.text
        message.hasPhoto() && message.caption != null -> message.caption
        message.hasVideo() && message.caption != null -> message.caption
        message.hasAudio() && message.caption != null -> message.caption
        message.hasDocument() && message.caption != null -> message.caption
        else -> throw TextNotProvided()
    }
    return inputText
}

fun MessageContent.extractText(): String =
    when (this.javaClass) {
        TdApi.MessageText::class.java -> (this as TdApi.MessageText).text.text
        TdApi.MessageAnimation::class.java -> (this as TdApi.MessageAnimation).caption.text
        TdApi.MessageAudio::class.java -> (this as TdApi.MessageAudio).caption.text
        TdApi.MessageDocument::class.java -> (this as TdApi.MessageDocument).caption.text
        TdApi.MessagePhoto::class.java -> (this as TdApi.MessagePhoto).caption.text
        TdApi.MessageVideo::class.java -> (this as TdApi.MessageVideo).caption.text
        else -> throw TextNotProvided()
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

fun Message.extractAttachmentInputFile(): InputFile? {
    return when {
        this.hasPhoto() -> InputFile(this.photo.last().fileId)
        this.hasVideo() -> InputFile(this.video.fileId)
        this.hasDocument() -> InputFile(this.document.fileId)
        this.hasAnimation() -> InputFile(this.animation.fileId)
        this.hasAudio() -> InputFile(this.audio.fileId)
        else -> null
    }
}

fun Update.extractAttachmentInputFile(): InputFile? =
    this.message?.extractAttachmentInputFile()

fun Message.extractAttachmentMediaBuilder(): InputMediaBuilder<*,*>? {
    return when {
        this.hasPhoto() -> InputMediaPhoto.builder().media(this.photo.last().fileId)
        this.hasVideo() -> InputMediaVideo.builder().media(this.video.fileId)
        this.hasDocument() -> InputMediaDocument.builder().media(this.document.fileId)
        this.hasAnimation() -> InputMediaAnimation.builder().media(this.animation.fileId)
        this.hasAudio() -> InputMediaAudio.builder().media(this.audio.fileId)
        else -> null
    }
}

fun Update.extractAttachmentMediaBuilder(): InputMediaBuilder<*,*>? =
    this.message?.extractAttachmentMediaBuilder()

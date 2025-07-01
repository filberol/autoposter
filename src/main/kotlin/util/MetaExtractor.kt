package ru.social.ai.util

import org.telegram.telegrambots.meta.api.objects.Update

fun Update.isReposted(): Boolean =
    this.message?.let { message ->
        message.forwardFrom != null ||
                message.forwardFromChat != null ||
                message.forwardFromMessageId != null ||
                message.forwardSenderName != null ||
                message.forwardSignature != null ||
                message.forwardDate != null
    } ?: false

fun Update.getUserId(): Long = this.message.from.id

fun Update.getChatId(): Long = this.message.chatId

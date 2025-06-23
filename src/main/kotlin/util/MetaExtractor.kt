package ru.social.ai.util

import org.telegram.telegrambots.meta.api.objects.Update

object MetaExtractor {
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

    fun getUserId(update: Update): Long {
        return update.message.from.id
    }
}

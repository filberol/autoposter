package ru.social.ai.util

import org.telegram.telegrambots.meta.api.objects.Update
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
    }
}

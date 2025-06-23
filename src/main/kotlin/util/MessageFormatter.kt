package ru.social.ai.util

object MessageFormatter {
    fun formatForTelegramMarkup(message: String): String {
        return message.replace("**", "*")
    }
}

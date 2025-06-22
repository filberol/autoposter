package ru.social.ai.util

class MessageFormatter {
    companion object {
        fun formatForTelegramMarkup(message: String): String {
            return message.replace("**", "*")
        }
    }
}
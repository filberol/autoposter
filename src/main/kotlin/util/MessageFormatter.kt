package ru.social.ai.util

fun String.formatForTelegramMarkup() =
    this.replace("**", "*")

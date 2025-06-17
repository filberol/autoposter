package ru.social.ai.prebuilders

import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

val SendMessagePreBuilder: SendMessage.SendMessageBuilder<*, *> = SendMessage.builder()
    .parseMode(ParseMode.MARKDOWN)


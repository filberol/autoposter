package ru.social.ai.util

import org.telegram.telegrambots.meta.api.methods.send.*
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.media.InputMedia
import ru.social.ai.clients.TelegramBot

private val telegramClient = TelegramBot.client

fun InputMedia.sendWithCaption(file: InputFile, chatId: String) {
    when (this.type) {
        "animation" ->
            SendAnimation.builder().chatId(chatId).animation(file).caption(this.caption).build()
                .also { telegramClient.execute(it) }

        "audio" ->
            SendAudio.builder().chatId(chatId).audio(file).caption(this.caption).build()
                .also { telegramClient.execute(it) }

        "document" ->
            SendDocument.builder().chatId(chatId).document(file).caption(this.caption).build()
                .also { telegramClient.execute(it) }

        "photo" ->
            SendPhoto.builder().chatId(chatId).photo(file).caption(this.caption).build()
                .also { telegramClient.execute(it) }

        "video" ->
            SendVideo.builder().chatId(chatId).video(file).caption(this.caption).build()
                .also { telegramClient.execute(it) }

        else -> return
    }
}

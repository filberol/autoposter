package ru.social.ai.util

import org.telegram.telegrambots.meta.api.methods.send.*
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.media.InputMedia
import ru.social.ai.clents.TelegramBot

class MediaUtils {
    companion object {
        private val telegramClient = TelegramBot.getClient()

        fun sendMediaWithCaption(media: InputMedia, file: InputFile, chatId: String) {
            when (media.type) {
                "animation" ->
                    SendAnimation.builder().chatId(chatId).animation(file).caption(media.caption).build()
                        .also { telegramClient.execute(it) }

                "audio" ->
                    SendAudio.builder().chatId(chatId).audio(file).caption(media.caption).build()
                        .also { telegramClient.execute(it) }

                "document" ->
                    SendDocument.builder().chatId(chatId).document(file).caption(media.caption).build()
                        .also { telegramClient.execute(it) }

                "photo" ->
                    SendPhoto.builder().chatId(chatId).photo(file).caption(media.caption).build()
                        .also { telegramClient.execute(it) }

                "video" ->
                    SendVideo.builder().chatId(chatId).video(file).caption(media.caption).build()
                        .also { telegramClient.execute(it) }

                else -> return
            }
        }
    }

}

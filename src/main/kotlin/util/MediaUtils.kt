package ru.social.ai.util

import it.tdlight.jni.TdApi
import org.telegram.telegrambots.meta.api.methods.send.*
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.media.InputMedia
import ru.social.ai.clients.MtProtoBot
import ru.social.ai.clients.TelegramBot
import java.io.File

private val telegramClient = TelegramBot.client
private val mtProtoClient = MtProtoBot.client

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

fun TdApi.Message.downloadImageIfPresent(): InputFile? {
    val photoContent = this.content as? TdApi.MessagePhoto
        ?: return null
    val photo = photoContent.photo.sizes.maxByOrNull { it.photo.expectedSize }
        ?: return null
    val fileId = photo.photo.id
    mtProtoClient.send(
        TdApi.DownloadFile(fileId, 1, 0, 0, true)
    ).get()
    val fileInfo = mtProtoClient.send(TdApi.GetFile(fileId)).get()
    val localPath = fileInfo?.local?.path ?: return null
    return InputFile(File(localPath))
}

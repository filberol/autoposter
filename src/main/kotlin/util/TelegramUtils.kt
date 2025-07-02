package ru.social.ai.util

import it.tdlight.jni.TdApi
import it.tdlight.jni.TdApi.Chat
import it.tdlight.jni.TdApi.GetChats
import it.tdlight.jni.TdApi.Message
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat
import org.telegram.telegrambots.meta.api.objects.chat.ChatFullInfo
import ru.social.ai.clients.MtProtoBot
import ru.social.ai.clients.TelegramBot
import ru.social.ai.exceptions.UserReasonableException

private val client = TelegramBot.client
private val mtProto = MtProtoBot.client

private fun String.removePrefixFromLink(): String =
    "@" + this
        .removePrefix("https://t.me/")
        .removePrefix("t.me/")
        .removePrefix("@")
        .trim()

fun getAdministratedChannelFromLink(channelLink: String): ChatFullInfo =
    client.execute(GetChat(channelLink.removePrefixFromLink()))

fun getSubscribedChannels(): List<Long> =
    mtProto.send(GetChats(null, 100))
        .get().chatIds.toList().filter { it != 777000L }

fun getPublicChannelFromLink(channelLink: Long): Chat =
    mtProto.send(TdApi.GetChat(channelLink)).get()

fun getLastMessageFromChannelLink(channelLink: Long): Message =
    getPublicChannelFromLink(channelLink).lastMessage

fun getMessageHistoryPublicChannel(channelLink: String, limit: Int): List<Message> {
    val chat = mtProto.send(TdApi.SearchPublicChat(channelLink.removePrefixFromLink())).get()
    val history = getHistory(chat.id, limit)
    return if (history.size == 1) {
        joinPublicChannel(chat.id)
        getHistory(chat.id, limit)
    } else {
        history
    }
}

private fun joinPublicChannel(channelId: Long) {
    val joinResult = mtProto.send(TdApi.JoinChat(channelId)).get()
    if (joinResult !is TdApi.Ok) {
        throw UserReasonableException("Невозможно присоединиться к каналу")
    }
}

private fun getHistory(channelLink: Long, limit: Int) = mtProto.send(
    TdApi.GetChatHistory(channelLink, 0, 0, limit, false)
).get().messages.toList()

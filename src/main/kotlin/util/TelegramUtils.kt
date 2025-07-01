package ru.social.ai.util

import it.tdlight.jni.TdApi
import it.tdlight.jni.TdApi.Chat
import it.tdlight.jni.TdApi.GetChats
import it.tdlight.jni.TdApi.Message
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat
import org.telegram.telegrambots.meta.api.objects.chat.ChatFullInfo
import ru.social.ai.clients.MtProtoBot
import ru.social.ai.clients.TelegramBot

private val client = TelegramBot.client
private val mtProto = MtProtoBot.client

fun getAdministratedChannelFromLink(channelLink: String): ChatFullInfo {
    val username = "@" + channelLink
        .removePrefix("https://t.me/")
        .removePrefix("t.me/")
        .removePrefix("@")
        .trim()
    return client.execute(GetChat(username))
}

fun getSubscribedChannels(): List<Long> =
    mtProto.send(GetChats(null, 100))
        .get().chatIds.toList().filter { it != 777000L }

fun getPublicChannelFromLink(channelLink: Long): Chat =
    mtProto.send(TdApi.GetChat(channelLink)).get()

fun getLastMessageFromChannelLink(channelLink: Long): Message =
    getPublicChannelFromLink(channelLink).lastMessage

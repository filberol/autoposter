package ru.social.ai.util

import it.tdlight.jni.TdApi.GetChats
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat
import org.telegram.telegrambots.meta.api.objects.chat.ChatFullInfo
import ru.social.ai.clients.MtProtoBot
import ru.social.ai.clients.TelegramBot

object TelegramUtils {
    private val client = TelegramBot.client
    private val mtProto = MtProtoBot.client

    fun getChannelFromLink(channelLink: String): ChatFullInfo {
        val username = "@" + channelLink
            .removePrefix("https://t.me/")
            .removePrefix("t.me/")
            .removePrefix("@")
            .trim()
        return client.execute(GetChat(username))
    }

    fun getSubscribedChannels(): List<Long> {
        return mtProto.send(GetChats()).get().chatIds.toList()
    }

    fun getChannelFromLink(channelLink: Long): ChatFullInfo {
        return client.execute(GetChat(channelLink.toString()))
    }
}

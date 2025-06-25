package ru.social.ai.util

import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat
import org.telegram.telegrambots.meta.api.objects.chat.ChatFullInfo
import ru.social.ai.clients.TelegramBot

object TelegramUtils {
    private val client = TelegramBot.getClient()

    fun getChannelFromLink(channelLink: String): ChatFullInfo {
        val username = "@" + channelLink
            .removePrefix("https://t.me/")
            .removePrefix("t.me/")
            .removePrefix("@")
            .trim()
        return client.execute(GetChat(username))
    }

    fun getChannelFromLink(channelLink: Long): ChatFullInfo {
        return client.execute(GetChat(channelLink.toString()))
    }
}

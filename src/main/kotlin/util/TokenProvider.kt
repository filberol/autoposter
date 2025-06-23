package ru.social.ai.util

object TokenProvider {
    fun getTelegramToken(): String = System.getenv("telegram_api_token")
    fun getFreeChatToken(): String = System.getenv("openrouter_api_token_free")
}

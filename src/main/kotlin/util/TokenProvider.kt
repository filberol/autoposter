package ru.social.ai.util

class TokenProvider {
    companion object {
        fun getToken(): String = System.getenv("telegram_api_token")
    }
}

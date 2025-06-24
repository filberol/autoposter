package ru.social.ai.commands.base

class CommandContext {
    private val data = mutableMapOf<String, Any?>()

    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: String): T? = data[key] as? T

    fun <T : Any> set(key: String, value: T) {
        data[key] = value
    }

    fun contains(key: String): Boolean = data.containsKey(key)
}

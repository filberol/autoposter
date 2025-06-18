package ru.social.ai.exceptions

open class UserReasonableException(
    message: String?,
) : RuntimeException(
    message ?: "Что-то пошло не так при обработке запроса. Попробуйте позже"
)

package ru.social.ai.commands.setup

import ru.social.ai.commands.base.multistage.Stage

object SetupI: Stage() {
    override val successPhrase = """
        Приступим к настройке! 
        Пришлите в любом удобном формате ссылку на канал. 
        Не забудьте, что бот должен быть администратором в канале. 
        """.trimIndent().replace("\n", "")
}

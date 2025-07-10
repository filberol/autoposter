package ru.social.ai.commands.base

import ru.social.ai.commands.base.context.CommandContext

interface WithContext {
    var context: CommandContext
}

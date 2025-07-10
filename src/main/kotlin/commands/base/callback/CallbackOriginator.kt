package ru.social.ai.commands.base.callback

import ru.social.ai.commands.base.Basic
import ru.social.ai.commands.base.context.CommandContext
import ru.social.ai.commands.base.WithContext

open class CallbackOriginator: Basic(), WithContext {
    override val triggerName = "command_not_found"
    override lateinit var context: CommandContext
}

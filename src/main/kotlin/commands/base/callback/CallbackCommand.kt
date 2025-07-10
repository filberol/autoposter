package ru.social.ai.commands.base.callback

import org.telegram.telegrambots.meta.api.methods.send.SendAnimation.SendAnimationBuilder
import org.telegram.telegrambots.meta.api.methods.send.SendAudio.SendAudioBuilder
import org.telegram.telegrambots.meta.api.methods.send.SendDocument.SendDocumentBuilder
import org.telegram.telegrambots.meta.api.methods.send.SendMediaBotMethod.SendMediaBotMethodBuilder
import org.telegram.telegrambots.meta.api.methods.send.SendMessage.SendMessageBuilder
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto.SendPhotoBuilder
import org.telegram.telegrambots.meta.api.methods.send.SendVideo.SendVideoBuilder
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow
import ru.social.ai.commands.base.Basic
import ru.social.ai.commands.base.context.CommandContext
import ru.social.ai.commands.base.WithContext

abstract class CallbackCommand: Basic(), WithContext {
    override val triggerName = "command_not_found"
    override lateinit var context: CommandContext
    abstract val buttonName: String

    protected fun SendMessageBuilder<*, *>.buildCallbacks(): SendMessageBuilder<*, *> {
        val callbacks  = callbacks.map {
            InlineKeyboardButton(it.value.buttonName ).apply { callbackData = it.key }
        }
        val markup = InlineKeyboardMarkup(listOf(InlineKeyboardRow(callbacks)))
        this.replyMarkup(markup)
        return this
    }

    protected fun SendMediaBotMethodBuilder<*, *, *>.buildCallbacks()
            : SendMediaBotMethodBuilder<*, *, *> {
        val callbacks  = callbacks.map {
            InlineKeyboardButton(it.value.buttonName ).apply { callbackData = it.key }
        }
        val markup = InlineKeyboardMarkup(listOf(InlineKeyboardRow(callbacks)))
        when (this) {
            is SendPhotoBuilder<*, *> -> {this.replyMarkup(markup); return this}
            is SendAnimationBuilder<*, *> -> {this.replyMarkup(markup); return this}
            is SendDocumentBuilder<*, *> -> {this.replyMarkup(markup); return this}
            is SendAudioBuilder<*, *> -> {this.replyMarkup(markup); return this}
            is SendVideoBuilder<*, *> -> {this.replyMarkup(markup); return this}
            else -> throw RuntimeException("Unsupported callback builder ${this.javaClass.canonicalName}")
        }
    }
}

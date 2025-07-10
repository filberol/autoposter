package ru.social.ai.commands.base.callback

import org.telegram.telegrambots.meta.api.methods.send.SendAnimation.SendAnimationBuilder
import org.telegram.telegrambots.meta.api.methods.send.SendAudio.SendAudioBuilder
import org.telegram.telegrambots.meta.api.methods.send.SendDocument.SendDocumentBuilder
import org.telegram.telegrambots.meta.api.methods.send.SendMediaBotMethod.SendMediaBotMethodBuilder
import org.telegram.telegrambots.meta.api.methods.send.SendMessage.SendMessageBuilder
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto.SendPhotoBuilder
import org.telegram.telegrambots.meta.api.methods.send.SendVideo.SendVideoBuilder
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow
import ru.social.ai.commands.base.Basic
import ru.social.ai.commands.base.context.CommandContext
import ru.social.ai.commands.base.context.CommandContextProvider
import ru.social.ai.commands.base.WithContext
import ru.social.ai.util.getUserId

abstract class WithCallback(
    override val triggerName: String
): Basic(), WithContext {
    abstract val callbacks: HashMap<String, CallbackCommand>
    abstract val originator: CallbackOriginator
    override lateinit var context: CommandContext

    override fun customTrigger(update: Update): Boolean {
        if (update.hasCallbackQuery()) {
            val query = update.callbackQuery.data
            return callbacks.keys.contains(query)
        }
        return false
    }

    override suspend fun execute(update: Update) {
        val userId = update.getUserId()
        val userContext = CommandContextProvider.getOrCreate(userId)

        if (update.hasCallbackQuery()) {
            val query = update.callbackQuery.data
            callbacks[query]?.let { command ->
                command.context = userContext
                command.execute(update)
            }
        } else {
            originator.let {
                it.context = userContext
                it.execute(update)
            }
        }
    }

    companion object {
        val callbacks by lazy {}
    }

}

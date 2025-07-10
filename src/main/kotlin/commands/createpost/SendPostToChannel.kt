package ru.social.ai.commands.createpost

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import ru.social.ai.commands.base.callback.CallbackCommand

class SendPostToChannel(
    override val buttonName: String
) : CallbackCommand() {
    override suspend fun execute(update: Update) {
        telegramClient.execute(
            SendMessage(
                update.message.chatId.toString(),
                "Кнопка нажата"
            )
        )
    }
}

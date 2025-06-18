package ru.social.ai.util

import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Update
import ru.social.ai.exceptions.TextNotProvided

class UpdateExtractor {
    companion object {
        fun extractText(update: Update): String {
            val message = update.message ?: throw TextNotProvided()
            val inputText = when {
                message.hasText() -> message.text
                message.hasPhoto() && message.caption != null -> message.caption
                message.hasVideo() && message.caption != null -> message.caption
                else -> throw TextNotProvided()
            }
            return inputText
        }

        fun extractTextWithoutCommand(update: Update): String {
            val text = extractText(update)
            return text.split(" ", limit = 2).getOrElse(1) { throw TextNotProvided() }
        }

        fun isTextPresent(update: Update): Boolean {
            return try {
                extractText(update)
                true
            } catch (e: TextNotProvided) {
                false
            }
        }

        fun isReposted(update: Update): Boolean {
            return update.message?.let { message ->
                message.forwardFrom != null ||
                        message.forwardFromChat != null ||
                        message.forwardFromMessageId != null ||
                        message.forwardSenderName != null ||
                        message.forwardSignature != null ||
                        message.forwardDate != null
            } ?: false
        }

        fun extractAttachments(update: Update): Pair<String?, List<InputFile>> {
            val message = update.message ?: return null to emptyList()
            val attachments = mutableListOf<InputFile>()
            val caption: String? = message.caption ?: message.text

            message.photo?.forEach { attachments.add(InputFile(it.fileId)) }
            message.video?.fileId?.let {
                attachments.add(InputFile(it))
            }
            message.document?.fileId?.let {
                attachments.add(InputFile(it))
            }
            message.audio?.fileId?.let {
                attachments.add(InputFile(it))
            }
            message.voice?.fileId?.let {
                attachments.add(InputFile(it))
            }
            message.sticker?.fileId?.let {
                attachments.add(InputFile(it))
            }

            return caption to attachments
        }
    }
}

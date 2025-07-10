package ru.social.ai

import ru.social.ai.commands.*
import ru.social.ai.commands.base.*
import ru.social.ai.commands.base.callback.WithCallback
import ru.social.ai.commands.base.multistage.MultiStage
import ru.social.ai.commands.common.*
import ru.social.ai.commands.createpost.*
import ru.social.ai.commands.debug.*
import ru.social.ai.commands.setup.*

val registeredCommands: List<Basic> = listOf(
    Test("/test"),
    ReplyDirectlyWithAi("/reply"),
    RephraseRepost("/rephrase"),
    Start("/start"),
    DebugUpdate("/debug"),
    MassMailing("/mass"),
    ProcessSources("/processSources"),
    ListClientSubscriptions("/listSubscriptions"),
    GetPublicChannelHistory("/channelHistory"),
    object : MultiStage("/setup") {
        override val stages = listOf(SetupI, SetupII, SetupIII, SetupIV)
    },
    object : WithCallback("/createPost") {
        override val originator = CreatePost
        override val callbacks = hashMapOf(
            "post" to SendPostToChannel("Отправляем!"),
            "cancel" to ReplyNotSending("Пропустить")
        )
    }
)

val cancelCommand = Cancel("/cancel")

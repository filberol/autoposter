package ru.social.ai

import ru.social.ai.commands.*
import ru.social.ai.commands.base.*
import ru.social.ai.commands.common.*
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
    CreatePost("/createPost"),
    object : MultiStage("/setup") {
        override val stages = listOf(SetupI, SetupII, SetupIII, SetupIV)
    }
)

val cancelCommand = Cancel("/cancel")

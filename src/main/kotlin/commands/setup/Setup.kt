package ru.social.ai.commands.setup

import ru.social.ai.commands.base.MultiStage

class Setup: MultiStage() {
    override val stages = listOf(SetupI())
}

package ru.social.ai.prebuilders

import io.github.sashirestela.openai.domain.chat.ChatRequest

val FreeModelPreBuilder: ChatRequest.ChatRequestBuilder = ChatRequest.builder()
    .model("deepseek/deepseek-chat-v3-0324:free")

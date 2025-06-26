package ru.social.ai.clients

import it.tdlight.Init
import it.tdlight.Log
import it.tdlight.Slf4JLogMessageHandler
import it.tdlight.client.*
import it.tdlight.tdnative.NativeClient
import java.nio.file.Paths

object MtProtoBot {
    private val apiId: String = System.getenv("telegram_api_id")
    private val apiHash: String = System.getenv("telegram_api_hash")
    private val apiUserId: String = System.getenv("telegram_api_user_id")

    val client: SimpleTelegramClient by lazy { initializeTdLightClient() }

    private fun initializeTdLightClient(): SimpleTelegramClient {
        // Initialize TDLight native libraries
        Init.init()
        Log.setLogMessageHandler(4, CustomLogger())

        val clientFactory = SimpleTelegramClientFactory()
        val apiToken = APIToken(apiId.toInt(), apiHash)
        val sessionPath = Paths.get("tdlib-session-id$apiUserId")
        val settings = TDLibSettings.create(apiToken).also {
            it.databaseDirectoryPath = sessionPath.resolve("data")
            it.downloadedFilesDirectoryPath = sessionPath.resolve("downloads")
        }
        val clientBuilder = clientFactory.builder(settings)
        val authenticationData = AuthenticationSupplier.qrCode()
        val client = clientBuilder.build(authenticationData)
        return client
    }

    class CustomLogger: NativeClient.LogMessageHandler {
        private val originalLogger = Slf4JLogMessageHandler()

        override fun onLogMessage(p0: Int, p1: String) {
            if (p1.contains("Begin to wait for updates")) return
            if (p1.contains("End to wait for updates")) return
            originalLogger.onLogMessage(p0, p1.trim())
        }
    }
}

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

    class CustomLogger : NativeClient.LogMessageHandler {
        private val originalLogger = Slf4JLogMessageHandler()

        private val filteredKeywords = listOf(
            "Begin to wait for updates",
            "End to wait for updates",
            "MessageDbActor",
            "ConnectionCreator",
            "&net_query",
            "ConfigManager",
            "NotificationManager",
            "DownloadManager",
            "UpdatesManager",
            "MessagesManager",
            "Session.cpp",
            "AttachMenuManager",
            "DialogDbActor",
            "FileManager.cpp",
            "StickersManager.cpp",
            "ContactsManager.cpp",
            "FileReferenceManager.cpp",
            "BackgroundManager.cpp",
            "DocumentsManager.cpp"
        )

        override fun onLogMessage(verbosityLevel: Int, message: String) {
            val trimmedMessage = message.trim()

            if (filteredKeywords.any { trimmedMessage.contains(it) }) {
                originalLogger.onLogMessage(5, trimmedMessage) // Send to trace level
            } else {
                originalLogger.onLogMessage(verbosityLevel, trimmedMessage)
            }
        }
    }
}

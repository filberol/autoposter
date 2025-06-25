package it.tdlight.example

import it.tdlight.Init
import it.tdlight.Log
import it.tdlight.Slf4JLogMessageHandler
import it.tdlight.client.*
import it.tdlight.jni.TdApi
import java.nio.file.Paths
import java.util.concurrent.TimeUnit

/**
 * Example class for TDLight Java
 *
 *
 * [The documentation of the TDLight functions can be found here](https://tdlight-team.github.io/tdlight-docs)
 */
object Example {
    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val adminId = Integer.getInteger("it.tdlight.example.adminid", 667900586).toLong()

        // Initialize TDLight native libraries
        Init.init()

        // Set the log level
        Log.setLogMessageHandler(1, Slf4JLogMessageHandler())

        SimpleTelegramClientFactory().use { clientFactory ->
            // Obtain the API token
            //
            // var apiToken = new APIToken(your-api-id-here, "your-api-hash-here");
            //
            val apiToken = APIToken.example()


            // Configure the client
            val settings = TDLibSettings.create(apiToken)

            // Configure the session directory.
            // After you authenticate into a session, the authentication will be skipped from the next restart!
            // If you want to ensure to match the authentication supplier user/bot with your session user/bot,
            //   you can name your session directory after your user id, for example: "tdlib-session-id12345"
            val sessionPath = Paths.get("example-tdlight-session")
            settings.databaseDirectoryPath = sessionPath.resolve("data")
            settings.downloadedFilesDirectoryPath = sessionPath.resolve("downloads")

            // Prepare a new client builder
            val clientBuilder = clientFactory.builder(settings)

            // Configure the authentication info
            // Replace with AuthenticationSupplier.consoleLogin(), or .user(xxx), or .bot(xxx);
            val authenticationData = AuthenticationSupplier.testUser(7381)
            // This is an example, remove this line to use the real telegram datacenters!
            settings.setUseTestDatacenter(true)
            ExampleApp(clientBuilder, authenticationData, adminId).use { app ->
                // Get me
                val me = app.client.meAsync[1, TimeUnit.MINUTES]

                // Create the "saved messages" chat
                val savedMessagesChat =
                    app.client.send(TdApi.CreatePrivateChat(me.id, true))[1, TimeUnit.MINUTES]

                // Send a test message
                val req = TdApi.SendMessage()
                req.chatId = savedMessagesChat.id
                val txt = TdApi.InputMessageText()
                txt.text = TdApi.FormattedText("TDLight test", arrayOfNulls(0))
                req.inputMessageContent = txt
                val result = app.client.sendMessage(req, true)[1, TimeUnit.MINUTES]
                println("Sent message:$result")
            }
        }
    }

    class ExampleApp(
        clientBuilder: SimpleTelegramClientBuilder,
        authenticationData: SimpleAuthenticationSupplier<*>?,
        /**
         * Admin user id, used by the stop command example
         */
        private val adminId: Long
    ) : AutoCloseable {
        val client: SimpleTelegramClient

        init {
            // Add an example update handler that prints when the bot is started
            clientBuilder.addUpdateHandler(
                TdApi.UpdateAuthorizationState::class.java
            ) { update: TdApi.UpdateAuthorizationState ->
                this.onUpdateAuthorizationState(
                    update
                )
            }

            // Add an example command handler that stops the bot
            clientBuilder.addCommandHandler<TdApi.Update>(
                "stop"
            ) { chat: TdApi.Chat, commandSender: TdApi.MessageSender, arguments: String ->
                this.onStopCommand(
                    chat,
                    commandSender,
                    arguments
                )
            }

            // Add an example update handler that prints every received message
            clientBuilder.addUpdateHandler(
                TdApi.UpdateNewMessage::class.java
            ) { update: TdApi.UpdateNewMessage -> this.onUpdateNewMessage(update) }

            // Build the client
            this.client = clientBuilder.build(authenticationData)
        }

        @Throws(Exception::class)
        override fun close() {
            client.close()
        }

        /**
         * Print the bot status
         */
        private fun onUpdateAuthorizationState(update: TdApi.UpdateAuthorizationState) {
            val authorizationState = update.authorizationState
            if (authorizationState is TdApi.AuthorizationStateReady) {
                println("Logged in")
            } else if (authorizationState is TdApi.AuthorizationStateClosing) {
                println("Closing...")
            } else if (authorizationState is TdApi.AuthorizationStateClosed) {
                println("Closed")
            } else if (authorizationState is TdApi.AuthorizationStateLoggingOut) {
                println("Logging out...")
            }
        }

        /**
         * Print new messages received via updateNewMessage
         */
        private fun onUpdateNewMessage(update: TdApi.UpdateNewMessage) {
            // Get the message content
            val messageContent = update.message.content

            // Get the message text
            val text = if (messageContent is TdApi.MessageText) {
                // Get the text of the text message
                messageContent.text.text
            } else {
                // We handle only text messages, the other messages will be printed as their type
                String.format("(%s)", messageContent.javaClass.simpleName)
            }

            val chatId = update.message.chatId

            // Get the chat title
            client.send(TdApi.GetChat(chatId)) // Use the async completion handler, to avoid blocking the TDLib response thread accidentally
                .whenCompleteAsync { chatIdResult: TdApi.Chat, error: Throwable? ->
                    if (error != null) {
                        // Print error
                        System.err.printf("Can't get chat title of chat %s%n", chatId)
                        error.printStackTrace(System.err)
                    } else {
                        // Get the chat name
                        val title = chatIdResult.title
                        // Print the message
                        System.out.printf("Received new message from chat %s (%s): %s%n", title, chatId, text)
                    }
                }
        }

        /**
         * Close the bot if the /stop command is sent by the administrator
         */
        private fun onStopCommand(chat: TdApi.Chat, commandSender: TdApi.MessageSender, arguments: String) {
            // Check if the sender is the admin
            if (isAdmin(commandSender)) {
                // Stop the client
                println("Received stop command. closing...")
                client.sendClose()
            }
        }

        /**
         * Check if the command sender is admin
         */
        fun isAdmin(sender: TdApi.MessageSender): Boolean {
            return if (sender is TdApi.MessageSenderUser) {
                sender.userId == adminId
            } else {
                false
            }
        }
    }
}

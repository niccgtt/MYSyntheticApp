package com.mysynthetic.agent.data.repository

import android.util.Log
import com.mysynthetic.agent.data.db.AppDatabase
import com.mysynthetic.agent.data.model.ChatMessage
import com.mysynthetic.agent.data.model.MessageEntity
import com.mysynthetic.agent.data.model.SessionEntity
import com.mysynthetic.agent.data.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.*

interface ChatRepository {
    fun getSessionMessages(sessionId: String): Flow<List<ChatMessage>>
    suspend fun sendMessage(sessionId: String, message: String, apiToken: String): Result<String>
    suspend fun createSession(title: String): String
    fun getAllSessions(): Flow<List<SessionEntity>>
    suspend fun deleteSession(sessionId: String)
    suspend fun getSessionById(sessionId: String): SessionEntity?
}

class ChatRepositoryImpl(
    private val database: AppDatabase,
    private val apiService: ApiService
) : ChatRepository {

    override fun getSessionMessages(sessionId: String): Flow<List<ChatMessage>> {
        return database.messageDao()
            .getMessagesForSession(sessionId)
            .map { entities ->
                entities.map { entity ->
                    ChatMessage(
                        id = entity.messageId,
                        role = entity.role,
                        content = entity.content,
                        timestamp = entity.timestamp
                    )
                }
            }
    }

    override suspend fun sendMessage(
        sessionId: String,
        message: String,
        apiToken: String
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            // Save user message
            val userMessage = MessageEntity(
                messageId = UUID.randomUUID().toString(),
                role = "user",
                content = message,
                timestamp = System.currentTimeMillis(),
                sessionId = sessionId
            )
            database.messageDao().insertMessage(userMessage)

            // Get conversation history
            val messageHistory = database.messageDao()
                .getMessagesForSessionStatic(sessionId, limit = 50)
                .map { ChatMessage(it.messageId, it.role, it.content, it.timestamp) }

            // Send to API
            val response = apiService.sendMessage(messageHistory, apiToken)

            return@withContext if (response.isSuccess) {
                val assistantContent = response.getOrNull() ?: return@withContext Result.failure(
                    Exception("Empty response")
                )

                // Save assistant message
                val assistantMessage = MessageEntity(
                    messageId = UUID.randomUUID().toString(),
                    role = "assistant",
                    content = assistantContent,
                    timestamp = System.currentTimeMillis(),
                    sessionId = sessionId
                )
                database.messageDao().insertMessage(assistantMessage)

                // Update session
                val messageCount = database.messageDao().getMessageCount(sessionId)
                database.sessionDao().updateSession(
                    SessionEntity(
                        sessionId = sessionId,
                        title = "", // Keep existing title
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis(),
                        messageCount = messageCount
                    )
                )

                Result.success(assistantContent)
            } else {
                Result.failure(response.exceptionOrNull() ?: Exception("Unknown error"))
            }
        } catch (e: Exception) {
            Log.e("ChatRepository", "Error sending message", e)
            Result.failure(e)
        }
    }

    override suspend fun createSession(title: String): String = withContext(Dispatchers.IO) {
        val sessionId = UUID.randomUUID().toString()
        val now = System.currentTimeMillis()
        val session = SessionEntity(
            sessionId = sessionId,
            title = title.ifEmpty { "Conversation ${Date(now)}" },
            createdAt = now,
            updatedAt = now,
            messageCount = 0
        )
        database.sessionDao().insertSession(session)
        sessionId
    }

    override fun getAllSessions(): Flow<List<SessionEntity>> {
        return database.sessionDao().getAllSessions()
    }

    override suspend fun deleteSession(sessionId: String) = withContext(Dispatchers.IO) {
        database.messageDao().deleteMessagesForSession(sessionId)
        database.sessionDao().deleteSession(sessionId)
    }

    override suspend fun getSessionById(sessionId: String): SessionEntity? {
        return database.sessionDao().getSessionById(sessionId)
    }
}

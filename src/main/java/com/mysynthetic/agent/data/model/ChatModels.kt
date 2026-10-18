package com.mysynthetic.agent.data.model

import kotlinx.serialization.Serializable
import androidx.room.Entity
import androidx.room.PrimaryKey

// API Models
@Serializable
data class ChatMessage(
    val id: String = "",
    val role: String, // "user" or "assistant"
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val toolCalls: List<ToolCall> = emptyList(),
    val toolResults: List<ToolResult> = emptyList()
)

@Serializable
data class ToolCall(
    val id: String,
    val name: String,
    val arguments: String
)

@Serializable
data class ToolResult(
    val toolCallId: String,
    val result: String,
    val isError: Boolean = false
)

@Serializable
data class ApiRequest(
    val messages: List<ApiMessage>,
    val model: String = "MY-Worker",
    val max_tokens: Int = 4096,
    val temperature: Double = 0.7
)

@Serializable
data class ApiMessage(
    val role: String,
    val content: String
)

@Serializable
data class ApiResponse(
    val id: String,
    val choices: List<Choice>,
    val usage: Usage?
)

@Serializable
data class Choice(
    val message: ApiMessage,
    val finish_reason: String?
)

@Serializable
data class Usage(
    val prompt_tokens: Int,
    val completion_tokens: Int,
    val total_tokens: Int
)

// Database Models
@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val messageId: String,
    val role: String,
    val content: String,
    val timestamp: Long,
    val sessionId: String,
    val toolCalls: String = "", // JSON serialized
    val toolResults: String = "" // JSON serialized
)

@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val sessionId: String,
    val title: String,
    val createdAt: Long,
    val updatedAt: Long,
    val messageCount: Int = 0
)

package com.mysynthetic.agent.data.network

import android.util.Log
import com.mysynthetic.agent.data.model.*
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor

interface ApiService {
    suspend fun sendMessage(messages: List<ChatMessage>, apiToken: String): Result<String>
}

class MyAiApiService(
    private val apiGatewayUrl: String = "https://api.my-ai.cn/v1/chat/completions"
) : ApiService {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    private val okHttpClient: OkHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Log.d("ApiService", message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .build()
    }

    override suspend fun sendMessage(
        messages: List<ChatMessage>,
        apiToken: String
    ): Result<String> = try {
        val apiMessages = messages.map { msg ->
            ApiMessage(
                role = msg.role,
                content = msg.content
            )
        }

        val request = ApiRequest(
            messages = apiMessages,
            model = "MY-Worker",
            max_tokens = 4096,
            temperature = 0.7
        )

        val requestBody = json.encodeToString(ApiRequest.serializer(), request)
            .toRequestBody("application/json".toMediaType())

        val httpRequest = Request.Builder()
            .url(apiGatewayUrl)
            .post(requestBody)
            .addHeader("Authorization", "Bearer $apiToken")
            .addHeader("Content-Type", "application/json")
            .build()

        val response = okHttpClient.newCall(httpRequest).execute()

        if (!response.isSuccessful) {
            return Result.failure(Exception("API Error: ${response.code} ${response.message}"))
        }

        val responseBody = response.body?.string() ?: return Result.failure(
            Exception("Empty response body")
        )

        val apiResponse = json.decodeFromString(ApiResponse.serializer(), responseBody)
        val assistantMessage = apiResponse.choices.firstOrNull()?.message?.content
            ?: return Result.failure(Exception("No message in response"))

        Result.success(assistantMessage)
    } catch (e: Exception) {
        Log.e("ApiService", "Error sending message", e)
        Result.failure(e)
    }
}

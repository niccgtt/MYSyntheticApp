package com.mysynthetic.agent.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mysynthetic.agent.data.model.ChatMessage
import com.mysynthetic.agent.data.model.SessionEntity
import com.mysynthetic.agent.data.repository.ChatRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentSessionId: String = "",
    val sessions: List<SessionEntity> = emptyList()
)

class ChatViewModel(
    private val repository: ChatRepository,
    private val apiToken: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    init {
        loadSessions()
    }

    fun setSessionId(sessionId: String) {
        _uiState.update { it.copy(currentSessionId = sessionId) }
        loadMessages(sessionId)
    }

    private fun loadMessages(sessionId: String) {
        viewModelScope.launch {
            repository.getSessionMessages(sessionId)
                .catch { error ->
                    _uiState.update {
                        it.copy(error = "Error loading messages: ${error.message}")
                    }
                }
                .collect { messages ->
                    _uiState.update {
                        it.copy(messages = messages, error = null)
                    }
                }
        }
    }

    private fun loadSessions() {
        viewModelScope.launch {
            repository.getAllSessions()
                .catch { error ->
                    _uiState.update {
                        it.copy(error = "Error loading sessions: ${error.message}")
                    }
                }
                .collect { sessions ->
                    _uiState.update {
                        it.copy(sessions = sessions)
                    }
                }
        }
    }

    fun sendMessage(message: String) {
        if (message.isBlank()) return

        val sessionId = _uiState.value.currentSessionId.ifEmpty {
            val newSessionId = createNewSession()
            _uiState.update { it.copy(currentSessionId = newSessionId) }
            newSessionId
        }

        _inputText.value = ""
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val result = repository.sendMessage(sessionId, message, apiToken)

            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = if (result.isFailure) {
                        result.exceptionOrNull()?.message ?: "Unknown error"
                    } else null
                )
            }

            if (result.isFailure) {
                _uiState.update {
                    it.copy(error = result.exceptionOrNull()?.message)
                }
            }
        }
    }

    private fun createNewSession(): String {
        var sessionId = ""
        viewModelScope.launch {
            sessionId = repository.createSession("New Conversation")
            _uiState.update { it.copy(currentSessionId = sessionId) }
            loadMessages(sessionId)
            loadSessions()
        }
        return sessionId
    }

    fun createSession(title: String) {
        viewModelScope.launch {
            val sessionId = repository.createSession(title)
            setSessionId(sessionId)
            loadSessions()
        }
    }

    fun deleteSession(sessionId: String) {
        viewModelScope.launch {
            repository.deleteSession(sessionId)
            if (_uiState.value.currentSessionId == sessionId) {
                _uiState.update {
                    it.copy(
                        currentSessionId = "",
                        messages = emptyList()
                    )
                }
            }
            loadSessions()
        }
    }

    fun updateInputText(text: String) {
        _inputText.value = text
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

class ChatViewModelFactory(
    private val repository: ChatRepository,
    private val apiToken: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            return ChatViewModel(repository, apiToken) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

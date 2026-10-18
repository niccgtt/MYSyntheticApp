package com.mysynthetic.agent.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mysynthetic.agent.data.model.ChatMessage
import com.mysynthetic.agent.ui.viewmodel.ChatViewModel

@Composable
fun ChatScreen(viewModel: ChatViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val inputText by viewModel.inputText.collectAsState()

    LaunchedEffect(Unit) {
        if (uiState.currentSessionId.isEmpty() && uiState.sessions.isNotEmpty()) {
            viewModel.setSessionId(uiState.sessions.first().sessionId)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top bar
        ChatTopBar(
            sessionCount = uiState.sessions.size,
            currentSession = uiState.sessions.find { it.sessionId == uiState.currentSessionId },
            onNewSession = { viewModel.createSession("New Conversation") },
            onShowSessions = { /* Will show session selector */ }
        )

        // Messages list
        MessagesList(
            messages = uiState.messages,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )

        // Error message
        if (uiState.error != null) {
            ErrorBanner(
                error = uiState.error!!,
                onDismiss = { viewModel.clearError() }
            )
        }

        // Input area
        ChatInputArea(
            inputText = inputText,
            onTextChange = { viewModel.updateInputText(it) },
            onSendClick = { viewModel.sendMessage(inputText) },
            isLoading = uiState.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
        )
    }
}

@Composable
fun ChatTopBar(
    sessionCount: Int,
    currentSession: com.mysynthetic.agent.data.model.SessionEntity?,
    onNewSession: () -> Unit,
    onShowSessions: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = "MYSynthetic AI",
                    style = MaterialTheme.typography.titleLarge
                )
                if (currentSession != null) {
                    Text(
                        text = currentSession.title,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = onShowSessions) {
                Icon(Icons.Default.MoreVert, "Sessions")
                Badge(
                    modifier = Modifier
                        .offset(x = (-8).dp, y = 8.dp)
                ) {
                    Text(sessionCount.toString())
                }
            }
            IconButton(onClick = onNewSession) {
                Icon(Icons.Default.Add, "New Chat")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
fun MessagesList(
    messages: List<ChatMessage>,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    if (messages.isEmpty()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Start a conversation with MYSynthetic AI",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    } else {
        LazyColumn(
            modifier = modifier
                .padding(8.dp),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { message ->
                MessageBubble(message = message)
            }
        }
    }
}

@Composable
fun MessageBubble(message: ChatMessage) {
    val isUser = message.role == "user"
    val backgroundColor = if (isUser) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    val contentColor = if (isUser) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(backgroundColor)
                .padding(12.dp)
        ) {
            Text(
                text = message.content,
                color = contentColor,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = java.text.SimpleDateFormat("HH:mm").format(message.timestamp),
                color = contentColor.copy(alpha = 0.7f),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
fun ChatInputArea(
    inputText: String,
    onTextChange: (String) -> Unit,
    onSendClick: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Divider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = inputText,
                onValueChange = onTextChange,
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 40.dp, max = 120.dp),
                placeholder = { Text("Type your message...") },
                enabled = !isLoading,
                shape = RoundedCornerShape(24.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
            IconButton(
                onClick = onSendClick,
                enabled = inputText.isNotBlank() && !isLoading,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        Icons.Default.Send,
                        "Send",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun ErrorBanner(error: String, onDismiss: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.errorContainer),
        color = MaterialTheme.colorScheme.errorContainer
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodySmall
            )
            IconButton(onClick = onDismiss) {
                Icon(
                    Icons.Default.Close,
                    "Dismiss",
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}

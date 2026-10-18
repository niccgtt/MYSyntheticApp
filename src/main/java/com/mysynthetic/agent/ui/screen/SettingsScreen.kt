package com.mysynthetic.agent.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    apiToken: String,
    apiEndpoint: String,
    onApiTokenChange: (String) -> Unit,
    onApiEndpointChange: (String) -> Unit,
    onBack: () -> Unit
) {
    var editingToken by remember { mutableStateOf(apiToken) }
    var editingEndpoint by remember { mutableStateOf(apiEndpoint) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Top bar
        TopAppBar(
            title = { Text("Settings") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, "Back")
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // API Token Section
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "API Configuration",
                        style = MaterialTheme.typography.titleMedium
                    )

                    OutlinedTextField(
                        value = editingToken,
                        onValueChange = { editingToken = it },
                        label = { Text("API Token") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        )
                    )

                    OutlinedTextField(
                        value = editingEndpoint,
                        onValueChange = { editingEndpoint = it },
                        label = { Text("API Endpoint") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Uri
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                onApiTokenChange(editingToken)
                                onApiEndpointChange(editingEndpoint)
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Save")
                        }
                        OutlinedButton(
                            onClick = {
                                editingToken = apiToken
                                editingEndpoint = apiEndpoint
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Reset")
                        }
                    }
                }
            }

            // About Section
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "About",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "MYSynthetic AI Agent v1.0.0",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "An Android client for controlling MYSynthetic AI via natural language",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Debug Info
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Debug Info",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Current Endpoint: $editingEndpoint",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Token Length: ${editingToken.length}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

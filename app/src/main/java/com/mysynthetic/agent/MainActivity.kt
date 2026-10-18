package com.mysynthetic.agent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.mysynthetic.agent.data.db.AppDatabase
import com.mysynthetic.agent.data.network.MyAiApiService
import com.mysynthetic.agent.data.repository.ChatRepositoryImpl
import com.mysynthetic.agent.ui.screen.ChatScreen
import com.mysynthetic.agent.ui.theme.MYSyntheticTheme
import com.mysynthetic.agent.ui.viewmodel.ChatViewModel
import com.mysynthetic.agent.ui.viewmodel.ChatViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize database
        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()

        // Initialize API service
        val apiService = MyAiApiService()

        // Initialize repository
        val repository = ChatRepositoryImpl(database, apiService)

        // Get API token from SharedPreferences or use default
        val sharedPref = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val apiToken = sharedPref.getString("api_token", "") 
            ?: "your-api-token-here"

        setContent {
            MYSyntheticTheme {
                // Create ViewModel with factory
                val viewModel: ChatViewModel = viewModel(
                    factory = ChatViewModelFactory(repository, apiToken)
                )

                ChatScreen(viewModel = viewModel)
            }
        }
    }
}

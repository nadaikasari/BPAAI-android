package com.nada.bpaai.di

import android.content.Context
import com.nada.bpaai.data.StoryRepository
import com.nada.bpaai.data.local.UserPreference
import com.nada.bpaai.data.remote.network.ApiConfig
import com.nada.bpaai.database.StoryDatabase

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(database, apiService)
    }
}
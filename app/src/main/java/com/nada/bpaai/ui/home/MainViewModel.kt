package com.nada.bpaai.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nada.bpaai.data.StoryRepository
import com.nada.bpaai.data.remote.response.ListStoryItem

class MainViewModel (private val storyRepository: StoryRepository) : ViewModel()  {

    val listStory: LiveData<List<ListStoryItem>> = storyRepository.getListStory

    val message: LiveData<String> = storyRepository.getMessage

    val isLoading: LiveData<Boolean> = storyRepository.isLoading

    fun getAllStory(token: String): LiveData<PagingData<ListStoryItem>> {
        return storyRepository.getAllStory(token).cachedIn(viewModelScope)
    }

    fun getStories(token: String) {
        storyRepository.getStory(token)
    }

}
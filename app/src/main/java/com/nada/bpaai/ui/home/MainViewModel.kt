package com.nada.bpaai.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nada.bpaai.data.StoryRepository
import com.nada.bpaai.data.local.UserModel
import com.nada.bpaai.data.local.UserPreference
import com.nada.bpaai.data.remote.response.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel (private val storyRepository: StoryRepository) : ViewModel()  {

    val message: LiveData<String> = storyRepository.getMessage

    val isLoading: LiveData<Boolean> = storyRepository.isLoading

    fun getAllStory(token: String): LiveData<PagingData<ListStoryItem>> {
        return storyRepository.getAllStory(token).cachedIn(viewModelScope)
    }


//    private val _listStory = MutableLiveData<StoriesResponse>()
//    val getListStory: LiveData<StoriesResponse> = _listStory
//
//    private val _message = MutableLiveData<String>()
//    val getMessage: LiveData<String> = _message
//
//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading
//
//    fun getStory(token: String) {
//        _isLoading.value = true
//
//        val client = ApiConfig.getApiService().getAllStories("Bearer $token")
//        client.enqueue(object : Callback<StoriesResponse> {
//            override fun onResponse(
//                call: Call<StoriesResponse>,
//                response: Response<StoriesResponse>
//            ) {
//                _isLoading.value = false
//                if (response.isSuccessful) {
//                    _listStory.value = response.body()
//                } else {
//                    _message.value = response.message()
//                }
//            }
//            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
//                _isLoading.value = false
//                _message.value = t.message.toString()
//            }
//        })
//    }
//
}
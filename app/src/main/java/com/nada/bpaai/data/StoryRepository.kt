package com.nada.bpaai.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.nada.bpaai.data.remote.network.ApiService
import com.nada.bpaai.data.remote.response.ListStoryItem
import com.nada.bpaai.database.StoryDatabase

class StoryRepository(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService
) {

//    private val _listStory = MutableLiveData<StoriesResponse>()
//    val getListStory: LiveData<StoriesResponse> = _listStory

    private val _message = MutableLiveData<String>()
    val getMessage: LiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

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
//    fun getDataUser(): LiveData<UserModel> {
//        return pref.getUser().asLiveData()
//    }
//
//    fun logout() {
//        viewModelScope.launch {
//            pref.logout()
//        }
//    }

    fun getAllStory(token: String): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, token),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }
}
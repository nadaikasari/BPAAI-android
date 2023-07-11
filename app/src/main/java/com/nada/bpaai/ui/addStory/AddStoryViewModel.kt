package com.nada.bpaai.ui.addStory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nada.bpaai.data.local.UserModel
import com.nada.bpaai.data.local.UserPreference
import com.nada.bpaai.data.remote.network.ApiConfig
import com.nada.bpaai.data.remote.response.AddNewStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStoryViewModel(private val pref: UserPreference) : ViewModel() {

    private val _response = MutableLiveData<AddNewStoryResponse>()
    val getResponse: LiveData<AddNewStoryResponse> = _response

    private val _message = MutableLiveData<String>()
    val getMessage: LiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun uploadStory (token: String, file: MultipartBody.Part, description: RequestBody) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().uploadStory("Bearer $token", file, description)
        client.enqueue(object : Callback<AddNewStoryResponse> {
            override fun onResponse(
                call: Call<AddNewStoryResponse>,
                response: Response<AddNewStoryResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    Log.e("addResponse", "onResponse: ${response.message()}")
                    _response.value = response.body()
                } else {
                    _message.value = response.message()
                }
            }
            override fun onFailure(call: Call<AddNewStoryResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = t.message.toString()
            }
        })
    }

    fun getDataUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }
}
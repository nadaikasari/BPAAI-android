package com.nada.bpaai.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nada.bpaai.data.local.UserModel
import com.nada.bpaai.data.local.UserPreference
import com.nada.bpaai.data.remote.network.ApiConfig
import com.nada.bpaai.data.remote.response.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginViewModel(private val pref: UserPreference) : ViewModel() {

    private val _dataUser = MutableLiveData<LoginResponse>()
    val getDataUser: LiveData<LoginResponse> = _dataUser

    private val _message = MutableLiveData<String>()
    val getMessage: LiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, password: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().userLogin(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _dataUser.value = response.body()
                } else {
                    _message.value = response.message()
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = t.message.toString()
            }
        })
    }

    fun saveUser(user: UserModel) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }

}
package com.nada.bpaai.ui.register

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nada.bpaai.data.remote.network.ApiConfig
import com.nada.bpaai.data.remote.response.RegisterResponse
import com.nada.bpaai.ui.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val getMessage: LiveData<String> = _message

    fun register(name: String, email: String, password: String, context: Context) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().userRegister(name, email, password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(context, intent, null)
                    _message.value = response.message()
                } else {
                    _message.value = response.message()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = t.message.toString()
            }
        })
    }
}
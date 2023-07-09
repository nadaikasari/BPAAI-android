package com.nada.bpaai.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nada.bpaai.data.local.UserModel
import com.nada.bpaai.data.local.UserPreference
import kotlinx.coroutines.launch

class UserViewModel (private val preference: UserPreference) : ViewModel() {

    fun getDataUser(): LiveData<UserModel> {
        return preference.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            preference.logout()
        }
    }
}
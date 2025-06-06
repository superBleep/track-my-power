package com.afca.trackmypower.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.afca.trackmypower.data.LocalDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = LocalDatabase.getInstance(application).userDAO

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> = _loginResult

    fun loginWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            val user = withContext(Dispatchers.IO) {
                userDao.getUserByEmailAndPassword(email, password)
            }
            _loginResult.value = (user != null)
        }
    }
}

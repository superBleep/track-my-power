package com.afca.trackmypower.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.afca.trackmypower.data.LocalDatabase
import com.afca.trackmypower.data.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = LocalDatabase.getInstance(application).userDAO

    private val _registrationResult = MutableLiveData<Boolean>()
    val registrationResult: LiveData<Boolean> = _registrationResult

    fun registerUser(newUser: User) {
        viewModelScope.launch {
            val success = withContext(Dispatchers.IO) {
                return@withContext try {
                    userDao.insertUser(newUser)
                    true
                } catch (e: Exception) {
                    false
                }
            }
            _registrationResult.value = success
        }
    }
}
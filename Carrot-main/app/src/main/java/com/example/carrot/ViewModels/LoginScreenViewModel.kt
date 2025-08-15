package com.example.carrot.ViewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carrot.AppManager.MainApplication
import com.example.carrot.AppManager.UserSession
import com.example.carrot.Models.User
import com.example.carrot.Repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    var phoneNumber: MutableState<String> = mutableStateOf("9999999991")

    fun onPhoneNumberChange(newPhoneNumber: String) {
        phoneNumber.value = newPhoneNumber
    }

    fun authenticateUser(callback: (Boolean) -> Unit) {
        if (phoneNumber.value.count() != 10) {
            callback(false)
        } else {
            viewModelScope.launch {
                val userExists = userRepository.login(phoneNumber = phoneNumber.value)
                callback(userExists)
            }
        }
    }

    fun loginWithEmailAction() {
        Log.d("LoginScreenViewModel", "Login with email action triggered")
    }

    fun setUserDetails() {
        viewModelScope.launch {
            val user = userRepository.getUserByPhoneNumber(phoneNumber = phoneNumber.value)
            if (user != null) {
                UserSession.setCurrentUser(user = user)
                println("User is set")
            } else {
                throw Exception("User not found")
            }
        }
    }

}
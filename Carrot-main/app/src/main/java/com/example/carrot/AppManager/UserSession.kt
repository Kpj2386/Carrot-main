package com.example.carrot.AppManager

import com.example.carrot.Models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object UserSession {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    fun setCurrentUser(user: User) {
        _currentUser.value = user
    }

    fun getCurrentUserPhoneNumber(): String? {
        return _currentUser.value?.phoneNumber
    }

    fun getCurrentUserId(): Int? {
        return _currentUser.value?.userId
    }

    fun getCurrentUser(): User {
        return _currentUser.value!!

    }

    fun logoutAction() {
        _currentUser.value = null

    }
}
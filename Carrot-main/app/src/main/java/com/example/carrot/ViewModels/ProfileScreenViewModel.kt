package com.example.carrot.ViewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.carrot.AppManager.UserSession
import com.example.carrot.Models.User
import com.example.carrot.Repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    val userObj: MutableState<User> = mutableStateOf(UserSession.getCurrentUser())

}
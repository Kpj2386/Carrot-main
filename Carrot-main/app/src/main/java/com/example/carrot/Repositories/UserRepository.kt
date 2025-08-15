package com.example.carrot.Repositories

import com.example.carrot.Models.User
import com.example.carrot.db.UserDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface UserRepository {
    suspend fun login(phoneNumber: String): Boolean
    suspend fun getUserByPhoneNumber(phoneNumber: String): User?
    suspend fun isUserExists(userID: String): Boolean
}

class UserRepositoryIml @Inject constructor(
    private val userDao: UserDao
) : UserRepository {
    override suspend fun login(phoneNumber: String): Boolean {
        return userDao.getUserByPhoneNumber(phoneNumber) != null
    }

    override suspend fun getUserByPhoneNumber(phoneNumber: String): User? {
        return userDao.getUserByPhoneNumber(phoneNumber)
    }

    override suspend fun isUserExists(userID: String): Boolean {
        return userDao.getUser(userId = userID) != null
    }
}
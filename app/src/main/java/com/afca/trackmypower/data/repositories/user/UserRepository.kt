package com.afca.trackmypower.data.repositories.user

import com.afca.trackmypower.data.dao.UserDAO
import com.afca.trackmypower.data.models.User

class UserRepository(private val userDAO: UserDAO) {

    suspend fun insert(user: User) {
        userDAO.insertUser(user)
    }

    suspend fun getUserByEmail(email: String, password: String): User? {
        return userDAO.getUserByEmailAndPassword(email, password)
    }
}
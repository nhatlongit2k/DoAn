package com.example.drivinglicenceadmin.repository

import android.app.Application
import com.example.drivinglicenceadmin.api.RetrofitHelper
import com.example.drivinglicenceadmin.model.User

class UserRepository(app: Application) {
    suspend fun getAllUsers(authToken: String) = RetrofitHelper.drivinglicenceAPI.getAllUsers(authToken)

    suspend fun getUsersWithRole(authToken: String, role: String) = RetrofitHelper.drivinglicenceAPI.getUsetsWithRole(authToken, role)

    suspend fun updateUser(authToken: String, user: User) = RetrofitHelper.drivinglicenceAPI.updateUser(authToken, user)

    suspend fun updateForUser(user: User) = RetrofitHelper.drivinglicenceAPI.updateForUser(user)

    suspend fun getAccount(authToken: String) = RetrofitHelper.drivinglicenceAPI.getAccount(authToken)

    suspend fun changeAccoungPassword(authToken: String, oldPassword: String, newPassword: String) = RetrofitHelper.drivinglicenceAPI.changeAccountPassword(authToken, oldPassword, newPassword)

    suspend fun registerAccountAdmin(userName: String, password: String, firstName: String, lastName: String, email: String, createBy: String, phoneNumber: String, image: String)
                = RetrofitHelper.drivinglicenceAPI.registerAccountAdmin(userName, password, firstName, lastName, email, createBy, phoneNumber, image)

    suspend fun registerAccount(userName: String, password: String, firstName: String, lastName: String, email: String, createBy: String, phoneNumber: String, image: String)
            = RetrofitHelper.drivinglicenceAPI.registerAccount(userName, password, firstName, lastName, email, createBy, phoneNumber, image)

    suspend fun getUserByLogin(login: String) = RetrofitHelper.drivinglicenceAPI.getUserByLogin(login)

    suspend fun resetPasswordForUser(newPassword: String, login: String) = RetrofitHelper.drivinglicenceAPI.resetPasswordForUser(newPassword, login)

    suspend fun updateAccount(user: User) = RetrofitHelper.drivinglicenceAPI.updateAccount(user)
}
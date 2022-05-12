package com.example.drivinglicenceadmin.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.drivinglicenceadmin.model.User
import com.example.drivinglicenceadmin.repository.UserRepository
import com.example.mvvm_retrofit.Resource
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import java.lang.IllegalArgumentException

class UserViewModel(application: Application): ViewModel() {
    private val userRepository : UserRepository = UserRepository(application)

    fun getAllUserFormApi(authToken: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(userRepository.getAllUsers(authToken)))
        }catch (e: Exception){
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }

    fun getUsersWithRole(authToken: String, role: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(userRepository.getUsersWithRole(authToken, role)))
        }catch (e: Exception){
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }

    fun updateUser(authToken: String, user: User) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(userRepository.updateUser(authToken, user)))
        }catch (e: Exception){
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }

    fun updateForUser(user: User) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(userRepository.updateForUser(user)))
        }catch (e: Exception){
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }

    fun getAccount(authToken: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(userRepository.getAccount(authToken)))
        }catch (e: Exception){
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }

    fun changeAccountPassword(authToken: String, oldPassword: String, newPassword: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(userRepository.changeAccoungPassword(authToken, oldPassword, newPassword)))
        }catch (e: Exception){
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }

    fun registerAccountAdmin(userName: String, password: String, firstName: String, lastName: String, email: String, createBy: String, phoneNumber: String, image: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(userRepository.registerAccountAdmin(userName, password, firstName, lastName, email, createBy, phoneNumber, image)))
        }catch (e: Exception){
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }

    fun registerAccoun(userName: String, password: String, firstName: String, lastName: String, email: String, createBy: String, phoneNumber: String, image: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(userRepository.registerAccount(userName, password, firstName, lastName, email, createBy, phoneNumber, image)))
        }catch (e: Exception){
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }

    fun getUserByLogin(login: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(userRepository.getUserByLogin(login)))
        }catch (e: Exception){
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }

    fun resetPasswordForUser(newPassword: String, login: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(userRepository.resetPasswordForUser(newPassword, login)))
        }catch (e: Exception){
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }

    fun updateAccount(user: User) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(userRepository.updateAccount(user)))
        }catch (e: Exception){
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }

    class UserViewModelFactory(private val application: Application): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(UserViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return UserViewModel(application) as T
            }
            throw IllegalArgumentException("Unable construce viewmodel")
        }
    }
}
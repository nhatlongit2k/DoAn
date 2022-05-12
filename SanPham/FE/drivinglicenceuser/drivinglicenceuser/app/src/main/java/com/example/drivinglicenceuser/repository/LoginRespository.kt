package com.example.drivinglicenceadmin.repository

import android.app.Application
import com.example.drivinglicenceadmin.api.RetrofitHelper
import com.example.drivinglicenceadmin.model.Login

class LoginRespository(app: Application) {
    suspend fun getToken(login: Login) = RetrofitHelper.drivinglicenceAPI.login(login)
}
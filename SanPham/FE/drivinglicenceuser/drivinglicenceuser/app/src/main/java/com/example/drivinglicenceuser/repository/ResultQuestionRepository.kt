package com.example.drivinglicenceuser.repository

import android.app.Application
import com.example.drivinglicenceadmin.api.RetrofitHelper

class ResultQuestionRepository(app: Application) {
    suspend fun getResultQuestionsByResultTestId(id: Long) = RetrofitHelper.drivinglicenceAPI.getResultQuestionsByResultTestId(id)
}
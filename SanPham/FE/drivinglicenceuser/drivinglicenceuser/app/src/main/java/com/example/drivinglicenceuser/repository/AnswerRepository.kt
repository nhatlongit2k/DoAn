package com.example.drivinglicenceadmin.repository

import android.app.Application
import com.example.drivinglicenceadmin.api.RetrofitHelper
import com.example.drivinglicenceadmin.model.Answer

class AnswerRepository(app: Application) {
    suspend fun getAnswerFromApi() = RetrofitHelper.drivinglicenceAPI.getAnswers()

    suspend fun getAnswerByQuestionId(id: Long) = RetrofitHelper.drivinglicenceAPI.getAnswersByQuestionId(id)

    suspend fun addAnswerToApi(answer: Answer) = RetrofitHelper.drivinglicenceAPI.addAnswers(answer)

    suspend fun deleteAnswerFromApi(id: Long) = RetrofitHelper.drivinglicenceAPI.deleteAnswerLicence(id)
}
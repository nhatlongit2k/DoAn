package com.example.drivinglicenceadmin.repository

import android.app.Application
import com.example.drivinglicenceadmin.api.RetrofitHelper
import com.example.drivinglicenceadmin.model.Answer
import com.example.drivinglicenceadmin.model.Question

class QuestionRepository(app: Application) {
    suspend fun getQuestionFromApi() = RetrofitHelper.drivinglicenceAPI.getQuestionsLicence()

    suspend fun getQuestionByExamId(id: Long) = RetrofitHelper.drivinglicenceAPI.getQuestionByExamId(id)

    suspend fun addQuestionToApi(question: Question) = RetrofitHelper.drivinglicenceAPI.createQuestionLiecence(question)

    suspend fun updateQuestion(id: Long, question: Question) = RetrofitHelper.drivinglicenceAPI.updateQuestionLicence(id, question)

    suspend fun deleteQuestion(id: Long) = RetrofitHelper.drivinglicenceAPI.deleteQuestionLicence(id)

    suspend fun updateQuestionWithListAnswer(question: Question) = RetrofitHelper.drivinglicenceAPI.updateQuestionWithListAnswer(question)

    suspend fun saveQuestionWithListAnswer(question: Question) = RetrofitHelper.drivinglicenceAPI.saveQuestionWithListAnswer(question)

    suspend fun saveAllQuestionWithAnswer(questionList: List<Question>) = RetrofitHelper.drivinglicenceAPI.saveAllQuestionWithAnswer(questionList)
}
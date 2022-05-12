package com.example.drivinglicenceadmin.repository

import android.app.Application
import com.example.drivinglicenceadmin.api.RetrofitHelper
import com.example.drivinglicenceadmin.model.Answer
import com.example.drivinglicenceadmin.model.Exam

class ExamRepository(app: Application) {
    suspend fun getExamFromApi() = RetrofitHelper.drivinglicenceAPI.getAllExamLicences()

    suspend fun createExamToApi(exam: Exam) = RetrofitHelper.drivinglicenceAPI.createExamLicence(exam)

    suspend fun updateExamToApi(id: Long, exam: Exam) = RetrofitHelper.drivinglicenceAPI.updateExamLicence(id, exam)

    suspend fun deleteExamFromApi(id: Long) = RetrofitHelper.drivinglicenceAPI.deleteExamLicence(id)

    suspend fun getExamForUser(authToken: String) = RetrofitHelper.drivinglicenceAPI.getExamForUser(authToken)

    suspend fun getExamForUserWithTypeId(authToken: String, id: Long) = RetrofitHelper.drivinglicenceAPI.getExamForUserWithTypeId(authToken, id)
}
package com.example.drivinglicenceuser.repository

import android.app.Application
import com.example.drivinglicenceadmin.api.RetrofitHelper
import com.example.drivinglicenceuser.model.ResultTest

class ResultTestRepository (app: Application) {

    suspend fun saveAllResult(resultTest: ResultTest) = RetrofitHelper.drivinglicenceAPI.saveAllResult(resultTest)

    suspend fun deleteResultTest(id: Long) = RetrofitHelper.drivinglicenceAPI.deleteResultTest(id)

}
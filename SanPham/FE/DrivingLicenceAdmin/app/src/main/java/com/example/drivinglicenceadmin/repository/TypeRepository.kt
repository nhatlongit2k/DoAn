package com.example.drivinglicenceadmin.repository

import android.app.Application
import com.example.drivinglicenceadmin.api.RetrofitHelper
import com.example.drivinglicenceadmin.model.Answer
import com.example.drivinglicenceadmin.model.Type

class TypeRepository(app: Application) {
    suspend fun getTypesFromApi() = RetrofitHelper.drivinglicenceAPI.getAllTypeLicences()

    suspend fun createTypeLicenceToApi(type: Type) = RetrofitHelper.drivinglicenceAPI.createTypeLicence(type)

    suspend fun updateTypeLicenceToApi(id: Long, type: Type) = RetrofitHelper.drivinglicenceAPI.updateTypeLicence(id, type)

    suspend fun deleteTypeLicenceOfApi(id: Long) = RetrofitHelper.drivinglicenceAPI.deleteTypeLicence(id)
}
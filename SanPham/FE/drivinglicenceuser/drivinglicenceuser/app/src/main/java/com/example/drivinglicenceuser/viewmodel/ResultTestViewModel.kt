package com.example.drivinglicenceuser.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.drivinglicenceuser.model.ResultTest
import com.example.drivinglicenceuser.repository.ResultTestRepository
import com.example.mvvm_retrofit.Resource
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import java.lang.IllegalArgumentException

class ResultTestViewModel(application: Application): ViewModel() {

    private val resultTestRepository: ResultTestRepository = ResultTestRepository(application)

    fun saveAllResult(resultTest: ResultTest) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(resultTestRepository.saveAllResult(resultTest)))
        }catch (e: Exception){
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }

    fun deleteResultTest(id: Long) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(resultTestRepository.deleteResultTest(id)))
        }catch (e: Exception){
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }

    class ResultTestViewModelFactory(private val application: Application): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(ResultTestViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return ResultTestViewModel(application) as T
            }
            throw IllegalArgumentException("Unable construce viewmodel")
        }
    }
}
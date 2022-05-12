package com.example.drivinglicenceuser.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.drivinglicenceadmin.viewmodel.AnswerViewModel
import com.example.drivinglicenceuser.repository.ResultQuestionRepository
import com.example.mvvm_retrofit.Resource
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import java.lang.IllegalArgumentException

class ResultQuestionViewModel(application: Application): ViewModel() {
    private val resultQuestionRepository: ResultQuestionRepository = ResultQuestionRepository(application)

    fun getResultQuestionsByResultTestId(id: Long) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(resultQuestionRepository.getResultQuestionsByResultTestId(id)))
        }catch (e: Exception){
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }

    class ResultQuestionViewModelFactory(private val application: Application): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(ResultQuestionViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return ResultQuestionViewModel(application) as T
            }
            throw IllegalArgumentException("Unable construce viewmodel")
        }
    }
}
package com.example.drivinglicenceadmin.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.drivinglicenceadmin.repository.AnswerRepository
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.liveData
import com.example.drivinglicenceadmin.model.Answer
import com.example.mvvm_retrofit.Resource
import java.lang.Exception
import java.lang.IllegalArgumentException

class AnswerViewModel(application: Application): ViewModel() {

    private val answerRepository: AnswerRepository = AnswerRepository(application)

    fun getAnswersFromApi() = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(answerRepository.getAnswerFromApi()))
        }catch (e: Exception){
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }

    fun addAnswerToApi(answer: Answer) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(answerRepository.addAnswerToApi(answer)))
        }catch (e: Exception){
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }

    fun getAnswerByQuestionId(id: Long) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(answerRepository.getAnswerByQuestionId(id)))
        }catch (e: Exception){
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }

    fun deleteAnswerFromApi(id: Long) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(answerRepository.deleteAnswerFromApi(id)))
        }catch (e: Exception){
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }

    class AnswerViewModelFactory(private val application: Application): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(AnswerViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return AnswerViewModel(application) as T
            }
            throw IllegalArgumentException("Unable construce viewmodel")
        }
    }
}
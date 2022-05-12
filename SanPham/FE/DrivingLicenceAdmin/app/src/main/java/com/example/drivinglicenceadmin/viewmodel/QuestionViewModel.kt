package com.example.drivinglicenceadmin.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.drivinglicenceadmin.model.Answer
import com.example.drivinglicenceadmin.model.Question
import com.example.drivinglicenceadmin.repository.QuestionRepository
import com.example.mvvm_retrofit.Resource
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import java.lang.IllegalArgumentException

class QuestionViewModel(application: Application): ViewModel() {

    private val questionrRepository: QuestionRepository = QuestionRepository(application)

    fun getQuestionFromApi() = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(questionrRepository.getQuestionFromApi()))
        }catch (e: Exception){
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }

    fun getQuestionByExamId(id: Long) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(questionrRepository.getQuestionByExamId(id)))
        }catch (e: Exception){
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }

    fun addQuestionToApi(question: Question) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(questionrRepository.addQuestionToApi(question)))
        }catch (e: Exception){
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }

    fun updateQuestionToApi(id: Long, question: Question) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(questionrRepository.updateQuestion(id, question)))
        }catch (e: Exception){
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }

    fun deleteQuestionFromApi(id: Long) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(questionrRepository.deleteQuestion(id)))
        }catch (e: Exception){
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }

    fun updateQuestionWithListAnswer(question: Question) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(questionrRepository.updateQuestionWithListAnswer(question)))
        }catch (e: Exception){
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }

    fun saveQuestionWithListAnswer(question: Question) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(questionrRepository.saveQuestionWithListAnswer(question)))
        }catch (e: Exception){
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }

    fun saveAllQuestionWithAnswer(questionList: List<Question>) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(questionrRepository.saveAllQuestionWithAnswer(questionList)))
        }catch (e: Exception){
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }

    class QuestionViewModelFactory(private val application: Application): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(QuestionViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return QuestionViewModel(application) as T
            }
            throw IllegalArgumentException("Unable construce viewmodel")
        }
    }
}
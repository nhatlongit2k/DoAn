package com.example.drivinglicenceadmin.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.drivinglicenceadmin.model.Exam
import com.example.drivinglicenceadmin.repository.AnswerRepository
import com.example.drivinglicenceadmin.repository.ExamRepository
import com.example.mvvm_retrofit.Resource
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import java.lang.IllegalArgumentException

class ExamViewModel(application: Application): ViewModel() {

    private val examRepository: ExamRepository = ExamRepository(application)

    fun getExamsFromApi() = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(examRepository.getExamFromApi()))
        }catch (e: Exception){
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }

    var examList: ArrayList<Exam> = ArrayList()

    fun createExamToApi(exam: Exam) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(examRepository.createExamToApi(exam)))
        }catch (e: Exception){
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }

    fun updateExamToApi(id: Long, exam: Exam) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(examRepository.updateExamToApi(id, exam)))
        }catch (e: Exception){
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }

    fun deleteExamFromApi(id: Long) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(examRepository.deleteExamFromApi(id)))
        }catch (e: Exception){
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }


    class ExamViewModelFactory(private val application: Application): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(ExamViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return ExamViewModel(application) as T
            }
            throw IllegalArgumentException("Unable construce viewmodel")
        }
    }
}
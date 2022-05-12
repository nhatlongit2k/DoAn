package com.example.drivinglicenceadmin.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.drivinglicenceadmin.model.Type
import com.example.drivinglicenceadmin.repository.TypeRepository
import com.example.mvvm_retrofit.Resource
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import java.lang.IllegalArgumentException

class TypeViewModel(application: Application): ViewModel() {
    private val typeRepository: TypeRepository = TypeRepository(application)

    fun getTypesFromApi() = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(typeRepository.getTypesFromApi()))
        }catch (e: Exception){
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }

    fun createTypeLicenceToApi(type: Type) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(typeRepository.createTypeLicenceToApi(type)))
        }catch (e: Exception){
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }

    fun updateTypeLicenceToApi(id: Long, type: Type) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(typeRepository.updateTypeLicenceToApi(id, type)))
        }catch (e: Exception){
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }

    fun deleteTypeLicenceOffApi(id: Long) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(typeRepository.deleteTypeLicenceOfApi(id)))
        }catch (e: Exception){
            emit(Resource.error(null, e.message ?: "Error"))
        }
    }

    class TypeViewModelFactory(private val application: Application): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(TypeViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return TypeViewModel(application) as T
            }
            throw IllegalArgumentException("Unable construce viewmodel")
        }
    }
}
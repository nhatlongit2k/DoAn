package com.example.drivinglicenceadmin.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    private const val BASE_URL = "http://54.255.148.110:8080/"

    private val builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
    val retrofit = builder.build()
    val drivinglicenceAPI: DrivinglicenceAPI = retrofit.create(DrivinglicenceAPI::class.java)
}
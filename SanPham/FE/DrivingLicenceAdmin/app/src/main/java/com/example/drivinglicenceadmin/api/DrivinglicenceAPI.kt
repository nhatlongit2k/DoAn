package com.example.drivinglicenceadmin.api

import com.example.drivinglicenceadmin.model.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface DrivinglicenceAPI {
    @POST("api/authenticate")
    suspend fun login(@Body login: Login): Token

    //Type
    @GET("api/types")
    suspend fun getAllTypeLicences(): List<Type>

    @POST("api/types")
    suspend fun createTypeLicence(@Body type: Type): Response<Unit>

    @PUT("api/types/{id}")
    suspend fun updateTypeLicence(@Path("id") id: Long, @Body type: Type): Response<Unit>

    @DELETE("api/types/{id}")
    suspend fun deleteTypeLicence(@Path("id") id:Long): ResultData

    //Exam
    @GET("api/exams")
    suspend fun getAllExamLicences(): List<Exam>

    @POST("api/exams")
    suspend fun createExamLicence(@Body exam: Exam): Exam

    @PUT("api/exams/{id}")
    suspend fun updateExamLicence(@Path("id") id: Long, @Body exam: Exam): Response<Unit>

    @DELETE("api/exams/{id}")
    suspend fun deleteExamLicence(@Path("id") id: Long): ResultData

    //user
    @GET("api/admin/users")
    suspend fun getAllUsers(@Header("Authorization") authToken: String): List<User>

    @GET("api/admin/users/getUserWithRole/{role}")
    suspend fun getUsetsWithRole(@Header("Authorization") authToken: String, @Path("role") role: String): List<User>

    @PUT("api/admin/users")
    suspend fun updateUser(@Header("Authorization") authToken: String, @Body user: User): User

    @GET("api/admin/users/getUserByLogin/{login}")
    suspend fun getUserByLogin(@Path("login") login: String): User

    @GET("api/admin/users/searchMultioption/{strSearch}")
    suspend fun searchMultioption(@Path("strSearch")strSearch: String): List<User>

    //account
    @GET("api/account")
    suspend fun getAccount(@Header("Authorization") authToken: String): User

    @POST("api/account/change-password-user")
    suspend fun changeAccountPassword(@Header("Authorization") authToken: String, @Query("oldPassword") oldPassword: String, @Query("newPassword") newPassword: String): Response<Unit>

    @POST("api/registerAccountAdmin")
    suspend fun registerAccountAdmin(@Query("userName") userName: String, @Query("password") password: String,
                                    @Query("firstName") firstName: String, @Query("lastName") lastName: String,
                                    @Query("email") email: String, @Query("createBy") createBy: String,
                                    @Query("phoneNumber") phoneNumber: String, @Query("image") image: String): Response<ResultRegisterAccount>?

    @POST("api/account")
    suspend fun updateAccount(@Body user: User): Response<Unit>

    @POST("api/account/reset-password-for-user")
    suspend fun resetPasswordForUser(@Query("newPassword") newPassword: String, @Query("login") login: String): Response<Unit>

    //question
    @GET("api/questions")
    suspend fun getQuestionsLicence(): List<Question>

    @GET("api/questions/getQuestionByExamId/{id}")
    suspend fun getQuestionByExamId(@Path("id") id: Long): List<Question>

    @POST("api/questions")
    suspend fun createQuestionLiecence(@Body question: Question): Question

    @PUT("api/questions/{id}")
    suspend fun updateQuestionLicence(@Path("id") id: Long, @Body question: Question): Response<Unit>

    @DELETE("api/questions/{id}")
    suspend fun deleteQuestionLicence(@Path("id") id: Long): ResultData

    @PUT("api/question/updateQuestionWithListAnswer")
    suspend fun updateQuestionWithListAnswer(@Body question: Question): ResultData

    @POST("api/question/saveQuestionWithListAnswer")
    suspend fun saveQuestionWithListAnswer(@Body question: Question): ResultData

    @POST("api/questions/saveAllQuestionsAndAnswers")
    suspend fun saveAllQuestionWithAnswer(@Body questionList: List<Question>): List<Question>

    //answer
    @GET("api/answers")
    suspend fun getAnswers(): List<Answer>

    @GET("api/answers/getAnswerByQuestionId/{id}")
    suspend fun getAnswersByQuestionId(@Path("id" )id: Long): List<Answer>

    @POST("api/answers")
    suspend fun addAnswers(@Body answer: Answer): Answer

    @PUT("api/answers/{id}")
    suspend fun updateAnswerLicence(@Path("id") id: Long, @Body answer: Answer): Response<Unit>

    @DELETE("api/answer/{id}")
    suspend fun deleteAnswerLicence(@Path("id") id: Long): ResultData

    @POST("api/answers/saveAllAnswers")
    suspend fun saveAllAnswer(@Body answerList: List<Answer>): Response<Unit>

    @GET("secretinfo")
    suspend fun getSecret(@Header("Authorization") authToken: String): Call<ResponseBody>
}
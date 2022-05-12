package com.example.drivinglicenceadmin.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Question(
    @SerializedName("id")
    var id: Long?,
    @SerializedName("content")
    var content: String,
    @SerializedName("image")
    var image: String?,
    @SerializedName("reason")
    var reason: String,
    @SerializedName("isFallQuestion")
    var isFallQuestion: Boolean,
    @SerializedName("examId")
    var examId: Long,
    @SerializedName("answerDTOList")
    var answers: ArrayList<Answer>?
): Serializable {
}
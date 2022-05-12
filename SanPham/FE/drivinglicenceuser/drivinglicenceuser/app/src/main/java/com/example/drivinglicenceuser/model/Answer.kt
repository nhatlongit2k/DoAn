package com.example.drivinglicenceadmin.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Answer(
    @SerializedName("id")
    var id: Long?,
    @SerializedName("content")
    var content: String,
    @SerializedName("questionId")
    var questionId: Long?,
    @SerializedName("isCorrect")
    var isCorrect: Boolean
):Serializable {
}
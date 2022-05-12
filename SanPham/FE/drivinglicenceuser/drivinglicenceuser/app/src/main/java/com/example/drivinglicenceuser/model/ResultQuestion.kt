package com.example.drivinglicenceuser.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ResultQuestion(
    @SerializedName("id")
    var id: Long?,
    @SerializedName("idAnswer")
    var idAnswer: Long?,
    @SerializedName("isCorrect")
    var isCorrect: Boolean?,
    @SerializedName("resulttestId")
    var resulttestId: Long?,
    @SerializedName("questionId")
    var questionId: Long?
): Serializable {
}
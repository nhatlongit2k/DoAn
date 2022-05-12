package com.example.drivinglicenceuser.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ResultTest(
    @SerializedName("idid")
    var id: Long?,
    @SerializedName("score")
    var score: Float,
    @SerializedName("numberCorrectQuestion")
    var numberCorrectQuestion: Long,
    @SerializedName("isPass")
    var isPass: Boolean,
    @SerializedName("userId")
    var userId: Long?,
    @SerializedName("examId")
    var examId: Long?,
    @SerializedName("resultQuestionDTOList")
    var resultQuestions: ArrayList<ResultQuestion>
): Serializable {
}
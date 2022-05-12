package com.example.drivinglicenceadmin.model

import com.google.gson.annotations.SerializedName

class ResultTest(
    @SerializedName("id")
    var id: Long?,
    @SerializedName("score")
    var score: Float?,
    @SerializedName("numberCorrectQuestion")
    var numberCorrectQuestion: Long?,
    @SerializedName("score")
    var isPass: Boolean,
    @SerializedName("userId")
    var userId: Long,
) {
}
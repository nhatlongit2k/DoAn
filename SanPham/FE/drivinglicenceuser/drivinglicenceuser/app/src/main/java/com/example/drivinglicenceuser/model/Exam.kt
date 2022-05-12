package com.example.drivinglicenceadmin.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.time.ZonedDateTime

class Exam(
    @SerializedName("id")
    var id: Long?,
    @SerializedName("name")
    var name: String,
    @SerializedName("numberOfQuestion")
    var numberOfQuestion: Long,
    @SerializedName("maxScore")
    var maxScore: Float,
    @SerializedName("time")
    var time: Long,
    @SerializedName("typeId")
    var typeId: Long?,
    @SerializedName("createBy")
    var createBy: String,
    @SerializedName("updateBy")
    var updateBy: String,
    @SerializedName("createTime")
    var createTime: String?,
    @SerializedName("updateTime")
    var updateTime: String?,
    @SerializedName("status")
    var status: Long?,
    @SerializedName("userId")
    var userId: Long?,
    @SerializedName("idResultTest")
    var idResultTest: Long?,
    @SerializedName("pass")
    var pass: Boolean
): Serializable {
}
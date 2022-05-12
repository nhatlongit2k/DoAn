package com.example.drivinglicenceadmin.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Type(
    @SerializedName("id")
    var id: Long?,
    @SerializedName("name")
    var name: String,
    @SerializedName("des")
    var des: String
    ): Serializable {
}
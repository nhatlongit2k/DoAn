package com.example.drivinglicenceadmin.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Token(
    @SerializedName("id_token")
    @Expose
    var token: String
    ): Serializable {
}